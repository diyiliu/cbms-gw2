package com.tiza.gw.support.bean;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-17 16:22)
 * Version: 1.0
 * Updated:
 */
public class ItemNodes {
    private String nameKey;
    private String name;
    private String type;
    private String endian;
    private String position;
    private int byteStart;
    private int byteLen;
    private int bitStart;
    private int bitLen;
    private String field;
    private String expression;
    //是否只去字节
    private boolean isOnlyByte;
    //是否空值 不更新
    private boolean isUpdateEmpty;

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEndian() {
        return endian;
    }

    public void setEndian(String endian) {
        this.endian = endian;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getByteStart() {
        return byteStart;
    }

    public void setByteStart(int byteStart) {
        this.byteStart = byteStart;
    }

    public int getByteLen() {
        return byteLen;
    }

    public void setByteLen(int byteLen) {
        this.byteLen = byteLen;
    }

    public int getBitStart() {
        return bitStart;
    }

    public void setBitStart(int bitStart) {
        this.bitStart = bitStart;
    }

    public int getBitLen() {
        return bitLen;
    }

    public void setBitLen(int bitLen) {
        this.bitLen = bitLen;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public boolean isOnlyByte() {
        return isOnlyByte;
    }

    public void setOnlyByte(boolean isOnlyByte) {
        this.isOnlyByte = isOnlyByte;
    }

    public boolean isUpdateEmpty() {
        return isUpdateEmpty;
    }

    public void setUpdateEmpty(boolean updateEmpty) {
        isUpdateEmpty = updateEmpty;
    }

    @Override
    public String toString() {
        return "ItemNodes{" +
                "nameKey='" + nameKey + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", endian='" + endian + '\'' +
                ", position='" + position + '\'' +
                ", byteStart=" + byteStart +
                ", byteLen=" + byteLen +
                ", bitStart=" + bitStart +
                ", bitLen=" + bitLen +
                ", field='" + field + '\'' +
                ", expression='" + expression + '\'' +
                ", isOnlyByte=" + isOnlyByte + '\'' +
                ", isUpdateEmpty=" + isUpdateEmpty +
                '}';
    }
}
