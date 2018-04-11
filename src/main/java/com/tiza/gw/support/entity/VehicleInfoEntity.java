package com.tiza.gw.support.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-11-06 14:59)
 * Version: 1.0
 * Updated:
 */
public class VehicleInfoEntity implements Serializable {
    private int vehicleId;
    private String vinCode;
    private int useStatus;
    private Date createTime;
    private int gpsInstallStatus;
    private String innerCode;
    private int productTypeId;
    private String productModel;
    private int ownerUserId;

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getGpsInstallStatus() {
        return gpsInstallStatus;
    }

    public void setGpsInstallStatus(int gpsInstallStatus) {
        this.gpsInstallStatus = gpsInstallStatus;
    }

    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
}
