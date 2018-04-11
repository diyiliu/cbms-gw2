package com.tiza.gw.support.utils;

import com.tiza.gw.support.thrift.ILocationService;
import com.tiza.gw.support.thrift.MapLocation;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-10-12 17:17)
 * Version: 1.0
 * Updated:
 */

public class ThriftUtils {
    private static Logger logger = LoggerFactory.getLogger(ThriftUtils.class);

    private String thriftIp;

    private Integer thriftPort;

    /**
     * @param latitude
     * @param longtitude
     * @param isS        是否南纬
     * @param isW        是否西经
     * @return
     */
    public MapLocation getArea(double latitude, double longtitude, boolean isS, boolean isW) {
        if (isS) {
            latitude = -latitude;
        }
        if (isW) {
            longtitude = -longtitude;
        }

        TTransport transport;
        MapLocation result = null;
        try {
            //设置为3秒超时
            transport = new TSocket(thriftIp, thriftPort , 3000);
            TProtocol protocol = new TCompactProtocol(transport);
            ILocationService.Client client = new ILocationService.Client(protocol);
            transport.open();
            result = client.getLocation(longtitude ,latitude);
            transport.close();
        } catch (TException e) {
            logger.error("获取省市区失败：" ,e);
            result = new MapLocation();
            result.setCountry("");
            result.setProvince("");
            result.setCity("");
            result.setTown("");
            result.setLatitude(0);
            result.setLongtitude(0);
        }
        return result;
    }

    /**
     * @param latitude
     * @param longtitude
     * @return
     */
    public MapLocation getArea(double latitude, double longtitude) {
        return getArea(latitude ,longtitude , false ,false );
    }

    public void setThriftIp(String thriftIp) {
        this.thriftIp = thriftIp;
    }

    public void setThriftPort(Integer thriftPort) {
        this.thriftPort = thriftPort;
    }
}
