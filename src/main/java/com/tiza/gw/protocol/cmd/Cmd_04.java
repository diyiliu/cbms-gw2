package com.tiza.gw.protocol.cmd;

import com.tiza.gw.protocol.CbBaseParser;
import com.tiza.gw.support.bean.FenceAlarmBean;
import com.tiza.gw.support.bean.GpsInfoBean;
import com.tiza.gw.support.bean.WorkAlarmBean;
import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.entity.VehicleInfoEntity;
import com.tiza.gw.support.task.job.FenceAlarmJob;
import com.tiza.gw.support.task.job.WorkAlarmJob;
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
import java.util.HashMap;
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
public class Cmd_04 extends CbBaseParser {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final int cmdId = 0x04;

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
        //控制器厂家编号/车
        int conVersion = bf.readShort();
        //CAN数据长度
        final int canLen = bf.readUnsignedShort();
        //处理功能集
        Map packageValues = new HashMap();
        if (bf.readableBytes() >= canLen) {
            byte[] packageBytes = new byte[canLen];
            bf.readBytes(packageBytes);
            packageValues = canUtils.parsePackage(packageBytes, softVersion, 4);

            if (monitorCacheProvider.containsKey(vinCode)) {
                LOGGER.warn("命令：4, 车辆[{}]: 状态位数据[{}]; CAN数据[{}]",
                        vinCode, CommonUtils.byteToString(statusBytes), CommonUtils.byteToString(packageBytes));
            }
        }
        //获取省市区
        final MapLocation area = thriftUtils.getArea(lat, lng);

        Map updateMap = new HashMap() {{
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
            //20170710 有工况的才更新 WORKDATATIME
             if(canLen > 20){
             this.put("WORKDATATIME", gpsTime);
            }
            this.put("SYSTEMTIME", new Date());
        }};

        updateMap.putAll(statusValues);
        updateMap.putAll(packageValues);

        //20161207 重构map
        updateMap=CanUtils.reBuldMap(updateMap);
        CommonUtils.dealInfoTODb(EnumConfig.DbConfig.VEHICLE_CURR, EnumConfig.DbConfig.DB_USER_CBMS, updateMap, new ConcurrentHashMap() {{
            this.put("VEHICLEID", entity.getVehicleId());
        }});

        onlineCacheProvider.put(vinCode, System.currentTimeMillis());

        FenceAlarmJob.sendData(new FenceAlarmBean(entity.getVehicleId(), lat, lng, gpsTime));
        WorkAlarmJob.sendData(new WorkAlarmBean(entity.getVehicleId(), lat, lng, gpsTime, softVersion, packageValues));
    }

    @Override
    public Integer getCmdId() {
        return this.cmdId;
    }
}
