package com.tiza.gw.support.utils;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-08-17 16:00)
 * Version: 1.0
 * Updated:
 */
public enum CanEnumConfig {
    CAN_4361(0x1301, 4, 8);


    private int type;
    private int packageIdLen;
    private int packageLen;
    CanEnumConfig(int type, int packageIdLen, int packageLen) {
        this.type = type;
        this.packageIdLen = packageIdLen;
        this.packageLen = packageLen ;
    }


    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("vehicle type is :").append(this.type).append(" ,package id len is :").append(this.packageIdLen).append(", package len is :").append(packageLen);
        return sb.toString();
    }

    public int getPackageIdLen() {
        return this.packageIdLen;
    }

    public int getPackageLen() {
        return this.packageLen;
    }

}
