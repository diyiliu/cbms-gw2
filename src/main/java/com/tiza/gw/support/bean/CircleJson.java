package com.tiza.gw.support.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-09-28 11:08)
 * Version: 1.0
 * Updated:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CircleJson implements Serializable {
    public CircleJson(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    private static final long serialVersionUID = -1820516436958844950L;
    /**
     * 经度log
     */
    @JsonProperty("lng")
    public double x;
    /**
     * 纬度lat
     */
    @JsonProperty("lat")
    public double y;
    @JsonProperty("radius")
    public double radius;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "CircleJson{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                '}';
    }
}
