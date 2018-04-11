package com.tiza.gw.support.bean;

import com.tiza.gw.support.utils.AlgorithmUtils;

import java.io.Serializable;


/**
 * 描述一个圆形区域
 *
 * @author Chauncey
 * @version $Revision: 1.0 $
 */

public class Circle implements Serializable, Area {
    /**
     * Field serialVersionUID. (value is -1032722048824397545)
     */
    private static final long serialVersionUID = -1032722048824397545L;
    /**
     * 中心点
     */
    private Point center;
    /**
     * 半径
     */
    private double radius;

    /**
     * Constructor for Circle.
     */
    public Circle() {
    }

    /**
     * Constructor for Circle.
     *
     * @param point  Point
     * @param radius int
     */
    public Circle(Point point, int radius) {
        this.center = point;
        this.radius = radius;
    }

    /**
     * Method getCenter.
     *
     * @return Point
     */
    public Point getCenter() {
        return this.center;
    }

    /**
     * Method getRadius.
     *
     * @return double
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Method setCenter.
     *
     * @param center Point
     */
    public void setCenter(Point center) {
        this.center = center;
    }

    /**
     * Method setRadius.
     *
     * @param radius double
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * 点与圆形区域的位置关系
     *
     * @param p 位置点
     * @return 0--在外，1--在内
     */
    @Override
    public int isPointInArea(Point p) {
        double distance = AlgorithmUtils.distanceP2P(p, this.center) * 1000;
        return ((distance > this.radius) ? 0 : 1);
    }
}