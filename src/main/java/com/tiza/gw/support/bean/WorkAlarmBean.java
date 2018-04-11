package com.tiza.gw.support.bean;

import java.util.Date;
import java.util.Map;

/**
 * Description: WorkAlarmBean
 * Author: DIYILIU
 * Update: 2016-02-01 10:11
 */
public class WorkAlarmBean {

    private int vehicleId;
    private double lat ;
    private double lng;
    private Date gpsTime;
    private String softVersion;
    private Map itemValue;

    public WorkAlarmBean() {
    }

    public WorkAlarmBean(int vehicleId, double lat, double lng, Date gpsTime, String softVersion, Map itemValue) {
        this.vehicleId = vehicleId;
        this.lat = lat;
        this.lng = lng;
        this.gpsTime = gpsTime;
        this.softVersion = softVersion;
        this.itemValue = itemValue;
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

    public Date getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(Date gpsTime) {
        this.gpsTime = gpsTime;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public Map getItemValue() {
        return itemValue;
    }

    public void setItemValue(Map itemValue) {
        this.itemValue = itemValue;
    }
}
