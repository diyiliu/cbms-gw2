package com.tiza.gw.support.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-06 10:19)
 * Version: 1.0
 * Updated:
 */
public class GpsInfoBean implements Serializable {
    private int infoLen;
    private int protocolVersion;
    private int comeFrom;
    private Date infoDate;
    private int infoType;
    private int cmdId;
    private byte[] messages;

    public GpsInfoBean(int infoLen, int protocolVersion, int comeFrom, Date infoDate, int infoType, int cmdId, byte[] messages) {
        this.infoLen = infoLen;
        this.protocolVersion = protocolVersion;
        this.comeFrom = comeFrom;
        this.infoDate = infoDate;
        this.infoType = infoType;
        this.cmdId = cmdId;
        this.messages = messages;
    }

    public byte[] getMessages() {
        return messages;
    }

    public void setMessages(byte[] messages) {
        this.messages = messages;
    }

    public int getInfoLen() {
        return infoLen;
    }

    public void setInfoLen(int infoLen) {
        this.infoLen = infoLen;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(int comeFrom) {
        this.comeFrom = comeFrom;
    }

    public Date getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(Date infoDate) {
        this.infoDate = infoDate;
    }

    public int getInfoType() {
        return infoType;
    }

    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }
}
