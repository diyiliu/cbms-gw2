package com.tiza.gw.support.utils;

import com.tiza.gw.netty.client.DbpClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-06 10:28)
 * Version: 1.0
 * Updated:
 */
public class CommonUtils {

    /**
     * @param dayByte
     * @return
     */
    public static Date getGpsTime(byte[] dayByte) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000 + dayByte[0], dayByte[1] - 1, dayByte[2], dayByte[3], dayByte[4], dayByte[5]);
        return calendar.getTime();
    }

    /**
     * 字节数组转换为整型
     *
     * @param b
     * @param byteLen
     * @return
     */
    public static int byte2Int(byte[] b, int byteLen) {
        int t = 8;
        int d = byteLen - 1;
        int and = 0xFF;
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & and) << (t * (d - i));
        }
        return intValue;
    }

    /**
     * 整形转换为 字节数组
     *
     * @param intValue
     * @param byteLen
     * @param isLittle 是否小端模式
     * @return
     */
    public static byte[] int2Byte(int intValue, int byteLen, boolean isLittle) {
        int t = 8;
        int d = byteLen - 1;
        int and = 0xFF;
        byte[] b = new byte[byteLen];
        if (isLittle) {
            for (int i = 0; i < byteLen; i++) {
                b[i] = (byte) (intValue >> t * i & and);
            }
        } else {
            for (int i = 0; i < byteLen; i++) {
                b[i] = (byte) (intValue >> t * (d - i) & and);
            }
        }
        //TODO 感觉效率不是很高对于少许的数据
        /*if(isLittle){
            ArrayUtils.reverse(b);
        }*/
        return b;
    }

    public static int byte2int(byte[] array) {

        if (array.length < 4){
            return byte2short(array);
        }

        int r = 0;
        for (int i = 0; i < array.length; i++)
        {
            r <<= 8;
            r |= array[i] & 0xFF;
        }

        return r;
    }

    public static short byte2short(byte[] array){

        short r = 0;
        for (int i = 0; i < array.length; i++)
        {
            r <<= 8;
            r |= array[i] & 0xFF;
        }

        return r;
    }



    public static int byteToInt(byte[] array) {
        int t = 8;
        int and = 0xFF;
        int intValue = 0;
        for (int i = 0, len = array.length; i < len; i++) {
            intValue += (array[i] & and) << (t * (len - i - 1));
        }
        if (intValue < 0) {
            return 0;
        }
        return intValue;
    }

    public static long byteToUnsignedInt(byte[] array) {

        return 0;
    }


    public static int getBits(int val, int start, int len) {
        int left = 31 - start;
        int right = 31 - len + 1;
        return (val << left) >>> right;
    }

    public static byte[] byteToByte(byte[] workParamBytes, int start, int len, String endian) {
        //int end = len - 1;
        byte[] tempBytes = new byte[len];
        int totalLen = start + len - 1;

        if (endian.equalsIgnoreCase("little")) {
            int tempI = 0;
            for (int j = totalLen; j >= start; j--) {// 小端模式
                tempBytes[tempI] = workParamBytes[j];
                tempI++;
            }
        } else {
            int tempI = 0;
            for (int j = start; j <= totalLen; j++) {// 大端模式
                tempBytes[tempI] = workParamBytes[j];
                tempI++;
            }
        }
        return tempBytes;
    }

    /**
     * 解析算数表达式
     *
     * @param exp
     * @return
     */
    public static String parseExp(int val, String exp, String type) throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        String retVal = "";
        if (type.equalsIgnoreCase("hex")) {
            retVal = String.format("%02X", val);
        } else if (type.equalsIgnoreCase("decimal")) {
            retVal = engine.eval(val + exp).toString();
        } else {
            //表达式解析会出现类型问题
            //System.out.println(engine.eval(val + exp).toString());
            //retVal = "" + ((Double) engine.eval(val + exp)).intValue();
            retVal = engine.eval(val + exp).toString();

        }
        return retVal;
    }


    public static String getTableName(String user, String tablePix, Date date, String formate) {
        //获取当前的年月
        String suff = DateFormatUtils.format(date.getTime(), formate);

        return user + "." + tablePix + suff;

    }

    public static String getTableName(String tablePix, String formate) {
        //获取当前的年月
        String suff = DateFormatUtils.format(System.currentTimeMillis(), formate);
        return tablePix + suff;
    }

    public static String getTableName(String tablePix, Date time, String formate) {
        //获取当前的年月
        String suff = DateFormatUtils.format(time, formate);
        return tablePix + suff;
    }

    /**
     * 新增
     *
     * @param table
     * @param user
     * @param values
     */
    public static void dealInfoTODb(String table, String user, Map values) {
        //组装SQL
        CreateSqlUtils createSqlUtils = new CreateSqlUtils();
        createSqlUtils.setTableName(table);
        createSqlUtils.setTableUser(user);
        createSqlUtils.setValues(values);
        createSqlUtils.setSqlType(1);
        createSqlUtils.createSql();
        DbpClient.sendSQL(createSqlUtils.getSql());
    }

    /**
     * 修改
     *
     * @param table
     * @param user
     * @param values
     * @param whereCase
     */
    public static void dealInfoTODb(String table, String user, Map values, Map whereCase) {
        //组装SQL
        CreateSqlUtils createSqlUtils = new CreateSqlUtils();
        createSqlUtils.setTableName(table);
        createSqlUtils.setTableUser(user);
        createSqlUtils.setValues(values);
        createSqlUtils.setWhereCase(whereCase);
        createSqlUtils.setSqlType(2);
        createSqlUtils.createSql();
        DbpClient.sendSQL(createSqlUtils.getSql());
    }

    public static void dealInfoTODb(String sql) {
        DbpClient.sendSQL(sql);
    }


    /**
     * 7转8数组算法
     *
     * @param content
     * @return
     */
    public static byte[] s2e(String content) {
        StringBuilder srcBuild = new StringBuilder();
        for (int i = 0, j = content.length(); i < j; i += 2) {
            String str = content.substring(i, i + 2);
            srcBuild.append(b2s((byte) Integer.parseInt(str, 16)).substring(1));
        }
        //System.out.println(srcBuild);
        byte[] dis = new byte[srcBuild.length() / 8];
        for (int i = 0; i < dis.length; i++) {
            String str = srcBuild.substring(i * 8, i * 8 + 8);
            dis[i] = (byte) Integer.parseInt(str, 2);
        }

        return dis;
       /* StringBuilder builder = new StringBuilder();
        for (byte b : dis) {
            builder.append(String.format("%02X", getNoSin(b)));
        }
        return builder.toString();*/
    }


    /**
     * 8转7数组算法
     *
     * @param src
     * @return
     */
    public static byte[] e2s(byte[] src) {
        StringBuffer buf = new StringBuffer();
        for (byte b : src) {
            buf.append(b2s(b));
        }
        StringBuffer fer = new StringBuffer();
        for (int i = 0, j = buf.length(); i < j; ) {
            if (i + 7 < j) {
                fer.append("0").append(buf.substring(i, i + 7));
                i += 7;
            } else {
                fer.append("0").append(buf.substring(i));
                while (i + 7 - j > 0) {
                    fer.append("0");
                    j++;
                }
                break;
            }
        }
        byte[] dis = new byte[fer.length() / 8];
        for (int i = 0; i < dis.length; i++) {
            String str = fer.substring(8 * i, 8 * (i + 1));
            dis[i] = (byte) Integer.parseInt(str, 2);
        }
        return dis;
    }

    /**
     * 字节转换成二进制字符串
     *
     * @param b
     * @return
     */
    private static String b2s(byte b) {
        StringBuffer str = new StringBuffer(Integer.toBinaryString(getNoSin(b)));
        int length = 8 - str.length();
        while (length > 0) {
            str.insert(0, "0");
            length--;
        }
        return str.toString();
    }

    /**
     * 获取字节的无符号数字
     *
     * @param b
     * @return
     */
    public static int getNoSin(byte b) {
        if (b >= 0) {
            return b;
        } else {
            return 256 + b;
        }
    }

    public static byte[] getDeviceID(String deviceID) {
        long value = Long.parseLong(deviceID, 10);
        byte[] data = new byte[4];
        data[3] = (byte) (value >> 24);
        data[2] = (byte) (value >> 16);
        data[1] = (byte) (value >> 8);
        data[0] = (byte) (value);
        return data;
    }

    public static int getCheckCode(byte[] bf) {
        int CheckCode = bf[0];
        for (int i = 1; i < bf.length; i++) {
            CheckCode = CheckCode ^ bf[i];
        }
        return CheckCode;
    }

    /**
     * 异或和校验
     *
     * @param content
     * @param checkNum
     * @return
     */
    public static boolean xorCheck(byte[] content, byte checkNum) {
        int sum = 0;
        for (byte temp : content) {
            sum = sum ^ temp;
        }
        if ((byte) sum == checkNum) {
            return true;
        }
        return false;
    }

    /**
     * @param content
     * @return
     */
    public static byte getXor(byte[] content) {
        int sum = 0;
        for (byte temp : content) {
            sum = sum ^ temp;
        }
        return (byte) sum;
    }

    public static long byteToLong(byte[] bytes, boolean isLittle) {
        int t = 8;
        long and = 0xFF;
        long intValue = 0L;
        int len = bytes.length;
        if (isLittle) {
            for (int i = len; i > 0; i--) {
                intValue += (bytes[i] & and) << (t * (len - i - 1));
            }
        } else {
            for (int i = 0; i < len; i++) {
                intValue += (bytes[i] & and) << (t * (len - i - 1));
            }
        }

        if (intValue < 0) {
            return 0L;
        }
        return intValue;
    }

    /**
     * 将long 转换为 字节数组
     *
     * @param val
     * @param byteLen
     * @param isLittle
     * @return
     */
    public static byte[] longToByte(long val, int byteLen, boolean isLittle) {
        int t = 8;
        int d = byteLen - 1;
        int and = 0xFF;
        byte[] b = new byte[byteLen];
        if (isLittle) {
            for (int i = 0; i < byteLen; i++) {
                b[i] = (byte) (val >> t * i & and);
            }
        } else {
            for (int i = 0; i < byteLen; i++) {
                b[i] = (byte) (val >> t * (d - i) & and);
            }
        }
        //TODO 感觉效率不是很高对于少许的数据
        /*if(isLittle){
            ArrayUtils.reverse(b);
        }*/
        return b;
    }

    public static byte getMessageTime(Date date)[] {
        ByteBuffer buf = ByteBuffer.allocate(6);
        Calendar value = Calendar.getInstance();
        value.setTime(date);
        byte b1 = (byte) (value.get(Calendar.YEAR) - 2000);
        byte b2 = (byte) (value.get(Calendar.MONTH) + 1);
        byte b3 = (byte) value.get(Calendar.DATE);
        byte b4 = (byte) value.get(Calendar.HOUR_OF_DAY);
        byte b5 = (byte) value.get(Calendar.MINUTE);
        byte b6 = (byte) value.get(Calendar.SECOND);
        buf.put(b1);
        buf.put(b2);
        buf.put(b3);
        buf.put(b4);
        buf.put(b5);
        buf.put(b6);
        return buf.array();
    }

    public static byte[] getDateBytes(Date date) {
        Calendar value = Calendar.getInstance();
        value.setTime(date);
        byte b1 = (byte) (value.get(Calendar.YEAR) - 2000);
        byte b2 = (byte) (value.get(Calendar.MONTH) + 1);
        byte b3 = (byte) value.get(Calendar.DATE);
        byte b4 = (byte) value.get(Calendar.HOUR_OF_DAY);
        byte b5 = (byte) value.get(Calendar.MINUTE);
        byte b6 = (byte) value.get(Calendar.SECOND);
        return new byte[]{b1, b2, b3, b4, b5, b6};
    }

    public static String byteToString(byte[] bytes) {
        StringBuffer buf = new StringBuffer();
        for (byte a : bytes) {
            buf.append(String.format("%02X", getNoSin(a))).append(" ");
        }
        return buf.substring(0, buf.length() - 1);
    }


    public static ByteBuf parkTlbs(byte[] content, int cmdId) {
        int len = EnumConfig.CommonConfig.MAIN_LENGTH + content.length;
        ByteBuf bf = Unpooled.buffer(len);
        bf.writeShort(len);
        bf.writeByte(EnumConfig.CommonConfig.PROTOCOL_VERSION);
        bf.writeByte(0);
        bf.writeBytes(getDateBytes(new Date()));
        bf.writeByte(EnumConfig.CommonConfig.INFO_TYPE_TLBS);
        bf.writeByte(cmdId);
        bf.writeBytes(content);
        return bf;
    }

    public static String parseVIN(byte[] array, int offset){

        ByteBuf buf = Unpooled.copiedBuffer(array);
        buf.readBytes(new byte[offset]);

        int len = buf.readByte();
        byte[] bytes = new byte[len];
        buf.readBytes(bytes);

        return new String(bytes);
    }
}
