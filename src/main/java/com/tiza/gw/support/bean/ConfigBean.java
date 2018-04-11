package com.tiza.gw.support.bean;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-22 15:24)
 * Version: 1.0
 * Updated:
 */
public class ConfigBean {
    private String outIp;
    private String inIp;
    private int outPort;
    private int inPort;

    public ConfigBean(String outIp, String inIp, int outPort, int inPort) {
        this.outIp = outIp;
        this.inIp = inIp;
        this.outPort = outPort;
        this.inPort = inPort;
    }

    public String getOutIp() {
        return outIp;
    }

    public void setOutIp(String outIp) {
        this.outIp = outIp;
    }

    public String getInIp() {
        return inIp;
    }

    public void setInIp(String inIp) {
        this.inIp = inIp;
    }

    public int getOutPort() {
        return outPort;
    }

    public void setOutPort(int outPort) {
        this.outPort = outPort;
    }

    public int getInPort() {
        return inPort;
    }

    public void setInPort(int inPort) {
        this.inPort = inPort;
    }
}
