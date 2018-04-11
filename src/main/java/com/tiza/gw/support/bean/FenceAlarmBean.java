package com.tiza.gw.support.bean;

import java.util.Date;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-10-26 14:54)
 * Version: 1.0
 * Updated:
 */
public class FenceAlarmBean {
    private int vehicleId;
    private double lat ;
    private double lng;
    private Date gpsTime;
    public FenceAlarmBean() {
    }

    public FenceAlarmBean(int vehicleId, double lat, double lng , Date gpsTime) {
        this.vehicleId = vehicleId;
        this.lat = lat;
        this.lng = lng;
        this.gpsTime = gpsTime;
    }

    public Date getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(Date gpsTime) {
        this.gpsTime = gpsTime;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
