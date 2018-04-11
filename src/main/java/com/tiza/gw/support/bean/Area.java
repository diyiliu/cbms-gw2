package com.tiza.gw.support.bean;

public interface Area {
	/**
	 * 判断经纬度点是否在区域内
	 * 
	 * @param p
	 *            位置点
	 * @return 等于0的时候在外,否则在内
	 */
	public int isPointInArea(Point p);
}
