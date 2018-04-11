package com.tiza.gw.support.utils;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-09 10:50)
 * Version: 1.0
 * Updated:
 */
public class EnumConfig {

    public static enum CommonConfig {
        ;
        public static final double LNG_LAT_DIVIDE = 1000000.000000;
        public static final byte PROTOCOL_VERSION = 0x01;
        public static final byte INFO_TYPE_TLBS = 0x20;
        public static final byte INFO_TYPE_GPS = 0x10;
        public static final short MAIN_LENGTH = 12;
    }

    public static enum DbConfig {
        ;
        public static final String VEHICLE_CURR = "BS_VEHICLEGPSINFO";
        public static final String VEHICLE_ALARM = "BS_VEHICLEALARM";
        public static final String DB_USER_CBMS = "CBMS";
    }

}
