package com.tiza.gw.support.bean;

import java.util.Date;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-10-28 11:27)
 * Version: 1.0
 * Updated:
 */
public class InOrOutBean {
    private double lng;
    private double lat;
    private Date time;
    private int inOrOut;

    public InOrOutBean() {
    }

    public InOrOutBean(double lng, double lat, Date time, int inOrOut) {
        this.lng = lng;
        this.lat = lat;
        this.time = time;
        this.inOrOut = inOrOut;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(int inOrOut) {
        this.inOrOut = inOrOut;
    }

    public String locationToString() {
        return this.lng + "," + this.lat;
    }
}
