package com.tiza.gw.support.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-20 14:04)
 * Version: 1.0
 * Updated:
 */
public class AlarmInfoEntity implements Serializable {
    private int id;
    private int vehicleId;
    private int alarmCategory;
    private Date startTime;
    private Date endTime;
    private double startLng;
    private double startLat;
    private double endLng;
    private double endLat;
    private int fenceId;
    private String nameKey;

    public AlarmInfoEntity() {
    }

    public AlarmInfoEntity(int vehicleId, int alarmCategory, Date startTime, Date endTime, double startLng, double startLat, double endLng, double endLat, int fenceId) {
        this.vehicleId = vehicleId;
        this.alarmCategory = alarmCategory;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLng = startLng;
        this.startLat = startLat;
        this.endLng = endLng;
        this.endLat = endLat;
        this.fenceId = fenceId;
    }

    public AlarmInfoEntity(int vehicleId, int alarmCategory, Date startTime, double startLng, double startLat, int fenceId) {
        this.vehicleId = vehicleId;
        this.alarmCategory = alarmCategory;
        this.startTime = startTime;
        this.startLng = startLng;
        this.startLat = startLat;
        this.fenceId = fenceId;
    }

    public AlarmInfoEntity(int vehicleId, int alarmCategory, Date startTime, double startLng, double startLat, String nameKey) {
        this.vehicleId = vehicleId;
        this.alarmCategory = alarmCategory;
        this.startTime = startTime;
        this.startLng = startLng;
        this.startLat = startLat;
        this.nameKey = nameKey;
    }

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

    public int getAlarmCategory() {
        return alarmCategory;
    }

    public void setAlarmCategory(int alarmCategory) {
        this.alarmCategory = alarmCategory;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public int getFenceId() {
        return fenceId;
    }

    public void setFenceId(int fenceId) {
        this.fenceId = fenceId;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }
}
