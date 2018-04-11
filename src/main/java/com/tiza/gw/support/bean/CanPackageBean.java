package com.tiza.gw.support.bean;

import java.util.List;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-17 16:16)
 * Version: 1.0
 * Updated:
 */
public class CanPackageBean {
    private List<ItemNodes> packageParams ;
    //can包数据长度
    private int packageParamsLen;

    private String packageId;

    public CanPackageBean(List<ItemNodes> packageParams, int packageParamsLen, String packageId) {
        this.packageParams = packageParams;
        this.packageParamsLen = packageParamsLen;
        this.packageId = packageId;
    }

    public CanPackageBean() {
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public List<ItemNodes> getPackageParams() {
        return packageParams;
    }

    public void setPackageParams(List<ItemNodes> packageParams) {
        this.packageParams = packageParams;
    }

    public int getPackageParamsLen() {
        return packageParamsLen;
    }

    public void setPackageParamsLen(int packageParamsLen) {
        this.packageParamsLen = packageParamsLen;
    }
}
