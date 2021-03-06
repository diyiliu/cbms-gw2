package com.tiza.gw.support.utils;

import com.tiza.gw.support.bean.Point;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-04-18 14:32)
 * Version: 1.0
 * Updated:
 */
public class AlgorithmUtils {
    /**
     * Field lock.
     */
    private static Object lock = new Object();

    /**
     * google maps的脚本里代码
     */

    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * google maps的脚本里代码
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 获取两点间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double twoPointDistance(double x1, double y1, double x2, double y2) {
        double temp_A, temp_B;
        double C;  // 用来储存算出来的斜边距离
        // 横向距离 (取正数，因为边长不能是负数)
        temp_A = Math.abs(x1 - x2);
        //x1 > x2 ? (x1 - x2) : (x2 - x1);
        // 竖向距离 (取正数，因为边长不能是负数)
        temp_B = Math.abs((y1 - y2));
        //y1 > y2 ? (y1 - y2) : (y2 - y1);
        // 斜边距离
        C = Math.sqrt(temp_A * temp_A + temp_B * temp_B);  // 计算
        return C;
    }

    /**
     * 判断点是否在多边形内
     *
     * @param point
     * @param polygonPoint
     * @return
     */
    public static boolean isInPolygon(Map point, List<Map> polygonPoint) {
       /* boolean flag = false;
        //lng:经度 lat:纬度
        double px = point.get(""),
                py = p.y,
                flag = false

        for (var i = 0, l = poly.length, j = l - 1; i < l; j = i, i++) {
            var sx = poly[i].x,
                    sy = poly[i].y,
                    tx = poly[j].x,
                    ty = poly[j].y

            // 点与多边形顶点重合
            if ((sx == = px && sy == = py) || (tx == = px && ty == = py)) {
                return 'on'
            }

            // 判断线段两端点是否在射线两侧
            if ((sy < py && ty >= py) || (sy >= py && ty < py)) {
                // 线段上与射线 Y 坐标相同的点的 X 坐标
                var x = sx + (py - sy) * (tx - sx) / (ty - sy)

                // 点在多边形的边上
                if (x == = px) {
                    return 'on'
                }

                // 射线穿过多边形的边界
                if (x > px) {
                    flag = !flag
                }
            }
        }

        // 射线穿过多边形边界的次数为奇数时点在多边形内
        return flag ? 'in' : 'out'

        return false;*/
        return false;
    }

    /**
     * 计算两点之间的距离
     *
     * @param p1
     * @param p2
     * @return double 单位(千米)
     */
    public static double distanceP2P(Point p1, Point p2) {
        synchronized (lock) {
            double alat = Math.abs(p1.getY());
            double alon = Math.abs(p1.getX());
            double blat = Math.abs(p2.getY());
            double blon = Math.abs(p2.getX());
            // 经纬度计算地球上任意两点间的距离的算法原理
            /*
             * 地球是一个近乎标准的椭球体，它的赤道半径为6378.137千米，极半径为6356.755千米，平均半径6371.004千米。
			 * 程序中我们假设地球是一个完美的球体，取其半径R=6378.137。
			 * 程序中以0度经线为基准，根据地球表面任意两点的经纬度计算出这两点间的地表距离
			 * （这里忽略地球表面地形对计算带来的误差，仅仅是理论上的估算值）。 设第一点A的经纬度为A(LonA,
			 * LatA)，第二点B的经纬度为B(LonB, LatB)；
			 * 按照0度经线的基准，东经取经度的正值(Longitude)，西经取经度负值(-Longitude
			 * )，北纬取90-纬度值(90-Latitude)，
			 * 南纬取90+纬度值(90+Latitude)，则经过上述处理过后的两点被计为(MLonA, MLatA)和(MLonB,
			 * MLatB)。那么根据三角推导，可以得到计算两点距离的如下公式：
			 * C=arccos(sin(LatA)*sin(LatB)*cos(LonA-LonB)+cos(LatA)*cos(LatB))
			 */
            // 如果仅对经度作正负的处理，而不对纬度作90-Latitude(假设都是北半球，南半球只有澳洲具有应用意义)的处理，那么公式将是：
            // C=sin(LatA)*sin(LatB) + cos(LatA)*cos(LatB)*cos(MLonA-MLonB)
            double R = 6378.137;// 地球半径(千米)
            // 判断点是在北半球还是南半球，本程序中输入的数据若为负则表示在南边球
            double distance = 0.0D;

            double _alat = (alat) * (Math.PI / 180); // 弧度
            double _alon = (alon) * (Math.PI / 180);
            double _blat = (blat) * (Math.PI / 180);
            double _blon = (blon) * (Math.PI / 180);

            double c = Math.sin(_alat) * Math.sin(_blat) + Math.cos(_alat) * Math.cos(_blat) * Math.cos(_alon - _blon); // Java中三角函数角度以弧度制表示
            if (c > 1) {
                c = 1;
            }
            distance = Math.acos(c) * R; // 弧长公式：弧长 = 弧度 * 半径
            if (distance <= 0.01) { // GPS误差
                distance = 0.0D;
            }
            return distance;
        }
    }

    /**
     * 计算两点之间的距离，不处理两点之间的偏差
     *
     * @param p1
     * @param p2
     * @return double 单位(千米)
     */
    public static double distanceP2PNoDeviation(Point p1, Point p2) {
        synchronized (lock) {
            double alat = Math.abs(p1.getY());
            double alon = Math.abs(p1.getX());
            double blat = Math.abs(p2.getY());
            double blon = Math.abs(p2.getX());

            // 经纬度计算地球上任意两点间的距离的算法原理
            /*
             * 地球是一个近乎标准的椭球体，它的赤道半径为6378.137千米，极半径为6356.755千米，平均半径6371.004千米。
			 * 程序中我们假设地球是一个完美的球体，取其半径R=6378.137。
			 * 程序中以0度经线为基准，根据地球表面任意两点的经纬度计算出这两点间的地表距离
			 * （这里忽略地球表面地形对计算带来的误差，仅仅是理论上的估算值）。 设第一点A的经纬度为A(LonA,
			 * LatA)，第二点B的经纬度为B(LonB, LatB)；
			 * 按照0度经线的基准，东经取经度的正值(Longitude)，西经取经度负值(-Longitude
			 * )，北纬取90-纬度值(90-Latitude)，
			 * 南纬取90+纬度值(90+Latitude)，则经过上述处理过后的两点被计为(MLonA, MLatA)和(MLonB,
			 * MLatB)。那么根据三角推导，可以得到计算两点距离的如下公式：
			 * C=arccos(sin(LatA)*sin(LatB)*cos(LonA-LonB)+cos(LatA)*cos(LatB))
			 */
            // 如果仅对经度作正负的处理，而不对纬度作90-Latitude(假设都是北半球，南半球只有澳洲具有应用意义)的处理，那么公式将是：
            // C=sin(LatA)*sin(LatB) + cos(LatA)*cos(LatB)*cos(MLonA-MLonB)
            double R = 6378.137;// 地球半径(千米)
            // 判断点是在北半球还是南半球，本程序中输入的数据若为负则表示在南边球
            double distance = 0.0D;

            double _alat = (alat) * (Math.PI / 180); // 弧度
            double _alon = (alon) * (Math.PI / 180);
            double _blat = (blat) * (Math.PI / 180);
            double _blon = (blon) * (Math.PI / 180);

            double c = Math.sin(_alat) * Math.sin(_blat) + Math.cos(_alat) * Math.cos(_blat) * Math.cos(_alon - _blon); // Java中三角函数角度以弧度制表示
            if (c > 1) {
                c = 1;
            }
            distance = Math.acos(c) * R; // 弧长公式：弧长 = 弧度 * 半径
            /*if (distance <= 0.01) { // GPS误差
                distance = 0.0D;
            }*/
            return distance;
        }
    }

    public static double distanceP2PNoDeviation(double alon, double alat, double blon, double blat) {
        synchronized (lock) {
            // 经纬度计算地球上任意两点间的距离的算法原理
            /*
             * 地球是一个近乎标准的椭球体，它的赤道半径为6378.137千米，极半径为6356.755千米，平均半径6371.004千米。
			 * 程序中我们假设地球是一个完美的球体，取其半径R=6378.137。
			 * 程序中以0度经线为基准，根据地球表面任意两点的经纬度计算出这两点间的地表距离
			 * （这里忽略地球表面地形对计算带来的误差，仅仅是理论上的估算值）。 设第一点A的经纬度为A(LonA,
			 * LatA)，第二点B的经纬度为B(LonB, LatB)；
			 * 按照0度经线的基准，东经取经度的正值(Longitude)，西经取经度负值(-Longitude
			 * )，北纬取90-纬度值(90-Latitude)，
			 * 南纬取90+纬度值(90+Latitude)，则经过上述处理过后的两点被计为(MLonA, MLatA)和(MLonB,
			 * MLatB)。那么根据三角推导，可以得到计算两点距离的如下公式：
			 * C=arccos(sin(LatA)*sin(LatB)*cos(LonA-LonB)+cos(LatA)*cos(LatB))
			 */
            // 如果仅对经度作正负的处理，而不对纬度作90-Latitude(假设都是北半球，南半球只有澳洲具有应用意义)的处理，那么公式将是：
            // C=sin(LatA)*sin(LatB) + cos(LatA)*cos(LatB)*cos(MLonA-MLonB)
            double R = 6378.137;// 地球半径(千米)
            // 判断点是在北半球还是南半球，本程序中输入的数据若为负则表示在南边球
            double distance = 0.0D;

            double _alat = (alat) * (Math.PI / 180); // 弧度
            double _alon = (alon) * (Math.PI / 180);
            double _blat = (blat) * (Math.PI / 180);
            double _blon = (blon) * (Math.PI / 180);

            double c = Math.sin(_alat) * Math.sin(_blat) + Math.cos(_alat) * Math.cos(_blat) * Math.cos(_alon - _blon); // Java中三角函数角度以弧度制表示
            if (c > 1) {
                c = 1;
            }
            distance = Math.acos(c) * R; // 弧长公式：弧长 = 弧度 * 半径
            /*if (distance <= 0.01) { // GPS误差
                distance = 0.0D;
            }*/
            return distance;
        }
    }

    /**
     * 计算点到线段的距离
     *
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double distPoint2Line(double x, double y, double x1, double y1, double x2, double y2) {
        double d1 = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);
        if (d1 < 0) {
            return distanceP2PNoDeviation(x, y, x1, y1);
        }
        double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (d1 >= d2) {
            return distanceP2PNoDeviation(x, y, x2, y2);
        }
        double r = d1 / d2;
        double px = x1 + (x2 - x1) * r;
        double py = y1 + (y2 - y1) * r;
        return distanceP2PNoDeviation(x, y, px, py);
    }

    /**
     * @param value
     * @param scale        小数点后的位数
     * @param roundingMode 模式，BigDecimal中的定义
     * @return
     */
    public static double dealDouble(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }
}
