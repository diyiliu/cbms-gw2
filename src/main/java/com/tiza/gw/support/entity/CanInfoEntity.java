package com.tiza.gw.support.entity;

import java.io.Serializable;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-17 14:19)
 * Version: 1.0
 * Updated:
 */
public class CanInfoEntity implements Serializable {
    private String softVersionCode;
    private String functionName;
    private String functionCode;
    private String xml;
    private String alertXml;

    public String getSoftVersionCode() {
        return softVersionCode;
    }

    public void setSoftVersionCode(String softVersionCode) {
        this.softVersionCode = softVersionCode;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getAlertXml() {
        return alertXml;
    }

    public void setAlertXml(String alertXml) {
        this.alertXml = alertXml;
    }
}
