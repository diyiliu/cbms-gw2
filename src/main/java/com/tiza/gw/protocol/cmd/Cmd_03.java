package com.tiza.gw.protocol.cmd;

import com.tiza.gw.protocol.CbBaseParser;
import com.tiza.gw.support.bean.FenceAlarmBean;
import com.tiza.gw.support.bean.GpsInfoBean;
import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.entity.VehicleInfoEntity;
import com.tiza.gw.support.task.job.FenceAlarmJob;
import com.tiza.gw.support.thrift.MapLocation;
import com.tiza.gw.support.utils.CanUtils;
import com.tiza.gw.support.utils.CommonUtils;
import com.tiza.gw.support.utils.EnumConfig;
import com.tiza.gw.support.utils.ThriftUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-09 10:50)
 * Version: 1.0
 * Updated:
 */
@Component
public class Cmd_03 extends CbBaseParser {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final int cmdId = 0x03;
    @Resource
    private CanUtils canUtils;
    @Resource(name = "vehicleInfoCacheProvider")
    private ICache vehicleCache;

    @Resource
    private ICache monitorCacheProvider;

    @Resource
    private ICache onlineCacheProvider;

    @Resource
    private ThriftUtils thriftUtils;

    @Override
    public void parser(Object message, Object ctx) {
        GpsInfoBean bean = (GpsInfoBean) message;
        ByteBuf bf = Unpooled.copiedBuffer(bean.getMessages());

        //车辆VIN码长度
        int vinLen = bf.readByte();
        //车辆VIN码
        byte[] vinBytes = new byte[vinLen];
        bf.readBytes(vinBytes);

        String vinCode = new String(vinBytes);
        final VehicleInfoEntity entity = (VehicleInfoEntity) vehicleCache.get(vinCode);
        if (null == entity) {
            LOGGER.debug("不存在该编号的车辆,vincode:{}", vinCode);
            return;
        }

        //软件版本号长度
        int softVersionLen = bf.readByte();

        //软件版本号
        byte[] softVersionBytes = new byte[softVersionLen];
        bf.readBytes(softVersionBytes);
        final String softVersion = new String(softVersionBytes);

        //经纬度
        final double lat = bf.readUnsignedInt() / EnumConfig.CommonConfig.LNG_LAT_DIVIDE;
        final double lng = bf.readUnsignedInt() / EnumConfig.CommonConfig.LNG_LAT_DIVIDE;
        final int speed = bf.readUnsignedByte();
        final int direction = bf.readUnsignedByte();
        final int altitude = bf.readUnsignedShort();

        int statusLen = bf.readByte();
        byte[] statusBytes = new byte[statusLen];
        bf.readBytes(statusBytes);
        byte[] timeByte = new byte[6];
        bf.readBytes(timeByte);
        final Date gpsTime = CommonUtils.getGpsTime(timeByte);
        Map statusValues = canUtils.parseStatus(statusBytes, softVersion);

        //获取省市区
        final MapLocation area = thriftUtils.getArea(lat, lng);

        if (monitorCacheProvider.containsKey(vinCode)) {
            LOGGER.warn("命令：3, 车辆[{}]: 状态位原始数据[{}]; 状态位解析值[{}]",
                    vinCode, CommonUtils.byteToString(statusBytes), statusValues);
        }

        Map updateMap = new ConcurrentHashMap() {{
            this.put("ENCRYPTLAT", lat);
            this.put("ENCRYPTLNG", lng);
            this.put("PROVINCE", area.getProvince());
            this.put("CITY", area.getCity());
            this.put("AREA", area.getTown());
            this.put("SPEED", speed);
            this.put("DIRECTION", direction);
            this.put("ALTITUDE", altitude);
            this.put("GPSTIME", gpsTime);
            this.put("SOFTVERSION", softVersion);
            this.put("SYSTEMTIME", new Date());
        }};
        updateMap.putAll(statusValues);
        CommonUtils.dealInfoTODb(EnumConfig.DbConfig.VEHICLE_CURR, EnumConfig.DbConfig.DB_USER_CBMS, updateMap, new ConcurrentHashMap() {{
            this.put("VEHICLEID", entity.getVehicleId());
        }});

        onlineCacheProvider.put(vinCode, System.currentTimeMillis());

        //分析围栏报警
        FenceAlarmJob.sendData(new FenceAlarmBean(entity.getVehicleId(), lat, lng, gpsTime));
    }

    @Override
    public Integer getCmdId() {
        return this.cmdId;
    }
}
