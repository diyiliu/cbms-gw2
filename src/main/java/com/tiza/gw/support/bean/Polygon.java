package com.tiza.gw.support.bean;

import java.io.Serializable;

/**
 * 描述一个多边形区域
 * 
 * @author Chauncey
 * 
 * @version $Revision: 1.0 $
 */

public class Polygon implements Serializable, Area {
	/**
	 * Field serialVersionUID. (value is 5750791836297904105)
	 */
	private static final long serialVersionUID = 5750791836297904105L;
	/** 顶点的个数 */
	private int nPoiCount;
	/** 顺时针，顶点的位置 */
	private Point[] pts;

	/**
	 * Method max.
	 * 
	 * @param x1
	 *            double
	 * @param x2
	 *            double
	 * @return double
	 */
	private double max(double x1, double x2) {
		return ((x1 > x2) ? x1 : x2);
	}

	/**
	 * Method min.
	 * 
	 * @param x1
	 *            double
	 * @param x2
	 *            double
	 * @return double
	 */
	private double min(double x1, double x2) {
		return ((x1 < x2) ? x1 : x2);
	}

	/**
	 * Constructor for Region.
	 */
	public Polygon() {
	}

	/**
	 * Constructor for Region.
	 * 
	 * @param points
	 *            Point[]
	 */
	public Polygon(Point[] points) {
		this.nPoiCount = points.length;
		this.pts = points;
	}

	/**
	 * Method getNPoiCount.
	 * 
	 * @return int
	 */
	public int getNPoiCount() {
		return this.nPoiCount;
	}

	/**
	 * Method getPts.
	 * 
	 * @return Point[]
	 */
	public Point[] getPts() {
		return this.pts;
	}

	/**
	 * Method setNPoiCount.
	 * 
	 * @param poiCount
	 *            int
	 */
	public void setNPoiCount(int poiCount) {
		this.nPoiCount = poiCount;
	}

	/**
	 * Method setPts.
	 * 
	 * @param pts
	 *            Point[]
	 */
	public void setPts(Point[] pts) {
		this.pts = pts;
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		return "GeoRegion:" + this.pts.length;
	}

	/**
	 * 0--点在区域外;1--点在区域内;2--点在顶点;3--点在区域边上.
	 */
	@Override
	public int isPointInArea(Point p) {
		double fX = p.getX();
		double fY = p.getY();
		int nCrossCount = 0;
		for (int i = 0; i < this.nPoiCount; ++i) {
			int nEndP;
			int nStartP = i;
			if (i + 1 == this.nPoiCount)
				nEndP = 0;
			else
				nEndP = i + 1;
			if ((fY > this.pts[nStartP].getY()) && (fY > this.pts[nEndP].getY()))
				continue;
			if ((fY < this.pts[nStartP].getY()) && (fY < this.pts[nEndP].getY()))
				continue;
			if (((Math.abs(fY - this.pts[nStartP].getY()) <= 0.00001) && (Math.abs(fX - this.pts[nStartP].getX()) <= 0.00001))
			        || ((Math.abs(fY - this.pts[nEndP].getY()) <= 0.00001) && (Math.abs(fX - this.pts[nEndP].getX()) <= 0.00001)))
				return 2;
			if ((Math.abs(fY - this.pts[nStartP].getY()) <= 0.00001)
			        && (Math.abs(fY - this.pts[nEndP].getY()) <= 0.0000)) {
				if (fX > max(this.pts[nStartP].getX(), this.pts[nEndP].getX()))
					continue;
				if ((fX < max(this.pts[nStartP].getX(), this.pts[nEndP].getX()))
				        && (fX > min(this.pts[nStartP].getX(), this.pts[nEndP].getX()))) {
					return 3;
				}

				if (i - 1 < 0) {
					nStartP = this.nPoiCount - 1;
				} else
					nStartP = i - 1;
				if (i + 2 >= this.nPoiCount)
					nEndP = i + 2 - this.nPoiCount;
				else
					nEndP = i + 2;
				++i;
				if ((fY < max(this.pts[nStartP].getY(), this.pts[nEndP].getY()))
				        || (fY > min(this.pts[nStartP].getY(), this.pts[nEndP].getY()))) {
					++nCrossCount;
				}

			} else if (Math.abs(fY - this.pts[nStartP].getY()) <= 0.0000) {
				if (fX < this.pts[nStartP].getX())
					++nCrossCount;

			} else if (Math.abs(fY - this.pts[nEndP].getY()) <= 0.0000) {
				nStartP = i;
				if (i + 2 >= this.nPoiCount)
					nEndP = i + 2 - this.nPoiCount;
				else
					nEndP = i + 2;
				if ((fY > max(this.pts[nStartP].getY(), this.pts[nEndP].getY()))
				        || (fY < min(this.pts[nStartP].getY(), this.pts[nEndP].getY())))
					++nCrossCount;

			} else {
				double fX0 = this.pts[nStartP].getX() + (this.pts[nEndP].getX() - this.pts[nStartP].getX())
				        * (fY - this.pts[nStartP].getY()) / (this.pts[nEndP].getY() - this.pts[nStartP].getY());
				if (Math.abs(fX0 - fX) < 0.00001d)
					return 3;
				if (fX0 > fX) {
					++nCrossCount;
				}
			}
		}
		return (nCrossCount % 2);
	}
}