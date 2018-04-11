package com.tiza.gw.support.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 2017/4/26.
 */
public class OnlineEntity implements Serializable {

    private int vehicleId;
    private int AccStatus;
    private Date createTime;

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getAccStatus() {
        return AccStatus;
    }

    public void setAccStatus(int accStatus) {
        AccStatus = accStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public OnlineEntity() {

    }

    public OnlineEntity(int vehicleId, int accStatus, Date createTime) {
        this.vehicleId = vehicleId;
        AccStatus = accStatus;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OnlineEntity{" +
                "vehicleId=" + vehicleId +
                ", AccStatus=" + AccStatus +
                ", createTime=" + createTime +
                '}';
    }
}
