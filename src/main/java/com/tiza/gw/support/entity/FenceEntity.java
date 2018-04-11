package com.tiza.gw.support.entity;

import java.io.Serializable;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-10-26 15:47)
 * Version: 1.0
 * Updated:
 */
public class FenceEntity implements Serializable {

    private int id;
    private int vehicleId;
    private String name;
    private int sharp;
    private String geoInfo;
    private int alarmType;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSharp() {
        return sharp;
    }

    public void setSharp(int sharp) {
        this.sharp = sharp;
    }

    public String getGeoInfo() {
        return geoInfo;
    }

    public void setGeoInfo(String geoInfo) {
        this.geoInfo = geoInfo;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
