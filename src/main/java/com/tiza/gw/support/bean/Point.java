package com.tiza.gw.support.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 描述一个点
 * 
 * @author Chauncey
 * 
 * @version $Revision: 1.0 $
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Point implements Serializable {
	/**
	 * Field serialVersionUID. (value is -2773320792876307488)
	 */
	private static final long serialVersionUID = -2773320792876307488L;
	/** 经度log */
    @JsonProperty("lng")
	public double x;
	/** 纬度lat */
    @JsonProperty("lat")
	public double y;

	public Point() {
	}

	/**
	 * Constructor for Point.
	 * 
	 * @param x
	 *            double
	 * @param y
	 *            double
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Method getX.
	 * 
	 * @return double
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Method getY.
	 * 
	 * @return double
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Method setX.
	 * 
	 * @param x
	 *            double
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Method setY.
	 * 
	 * @param y
	 *            double
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		return "Point(x=" + getX() + ", y=" + getY() + ")";
	}
}