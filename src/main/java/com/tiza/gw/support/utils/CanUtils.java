package com.tiza.gw.support.utils;

import com.tiza.gw.support.bean.AlertBean;
import com.tiza.gw.support.bean.CanPackageBean;
import com.tiza.gw.support.bean.ItemNodes;
import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.entity.CanInfoEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 功能集解析
 * Author: Wolf
 * Created:Wolf-(2014-12-17 16:01)
 * Version: 1.0
 * Updated:
 */
@Component
public class CanUtils {
    @Resource(name = "canPackageProvider")
    private ICache canPackageProvider;

    @Resource(name = "canStatusProvider")
    private ICache canStatusProvider;

    @Resource(name = "paramsCacheProvider")
    private ICache paramsCacheProvider;

    @Resource(name = "nameKeyCacheProvider")
    private ICache nameKeyCacheProvider;

    @Resource(name = "alertItemCacheProvider")
    private ICache alertItemCacheProvider;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void dealCan(CanInfoEntity canInfo) {
        Document doc = null;
        if (StringUtils.isEmpty(canInfo.getXml())) {
            return;
        }

        try {
            doc = DocumentHelper.parseText(canInfo.getXml());
            List<Node> rootPackageNode = doc.selectNodes("/root/can/*");
            List<Node> rootStatusNode = doc.selectNodes("/root/status/*");
            if (null != rootStatusNode) {
                List<ItemNodes> status = dealStatus(rootStatusNode);
                canStatusProvider.put(canInfo.getSoftVersionCode(), status);

            }
            if (null != rootPackageNode) {
                Map<String, CanPackageBean> packages = dealPackage(rootPackageNode, canInfo.getSoftVersionCode());
                canPackageProvider.put(canInfo.getSoftVersionCode(), packages);
            }

            if (null != canInfo.getAlertXml()) {
                doc = DocumentHelper.parseText(canInfo.getAlertXml());
                List<Node> rootAlertNode = doc.selectNodes("/root/alert");
                if (null != rootAlertNode) {
                    Map<String, AlertBean> alertItems = dealAlertNodes(rootAlertNode, canInfo.getSoftVersionCode());
                    alertItemCacheProvider.put(canInfo.getSoftVersionCode(), alertItems);
                }
            }
        } catch (Exception e) {
            logger.error("解析功能集错误[" + canInfo.getSoftVersionCode() + "]:", e);
            logger.error(canInfo.getXml());
        }
    }

    private ItemNodes dealItems(Node itemNode) {
        ItemNodes itemBean = new ItemNodes();
        String nameKey = itemNode.selectSingleNode("nameKey").getText();
        String name = itemNode.selectSingleNode("name").getText();
        String type = itemNode.selectSingleNode("type").getText();
        String endian = itemNode.selectSingleNode("endian") == null ? "big" : itemNode.selectSingleNode("endian").getText();
        Node position = itemNode.selectSingleNode("position");
        Node byteNode = position.selectSingleNode("byte");
        Node bitNode = byteNode.selectSingleNode("bit");
        String byteStart = byteNode.valueOf("@start");
        String byteLen = byteNode.valueOf("@length");
        boolean isUpdateEmpty;
        try {
            isUpdateEmpty = Boolean.valueOf(byteNode.valueOf("@isUpdateEmpty"));
        } catch (Exception e) {
            isUpdateEmpty = true;
        }
        if (null == bitNode) {
            itemBean.setOnlyByte(true);
        } else {
            itemBean.setOnlyByte(false);
            String bitStart = bitNode.valueOf("@start");
            String bitLen = bitNode.valueOf("@length");
            itemBean.setBitStart(Integer.parseInt(bitStart));
            itemBean.setBitLen(Integer.parseInt(bitLen));
        }
        String expression = itemNode.selectSingleNode("expression").getText();
        String field = itemNode.selectSingleNode("field").getText();
        itemBean.setNameKey(nameKey);
        itemBean.setName(name);
        itemBean.setType(type);
        itemBean.setEndian(endian);
        itemBean.setByteStart(Integer.parseInt(byteStart));
        itemBean.setByteLen(Integer.parseInt(byteLen));
        itemBean.setExpression(expression);
        itemBean.setField(field);

        itemBean.setUpdateEmpty(isUpdateEmpty);
        return itemBean;
    }

    private List<ItemNodes> dealStatus(List<Node> nodes) {
        List<ItemNodes> items = new ArrayList<ItemNodes>(nodes.size());
        for (Node itemNode : nodes) {
            items.add(this.dealItems(itemNode));
        }
        return items;
    }


    private Map<String, CanPackageBean> dealPackage(List<Node> packages, String codeName) {
        int packageNum = packages.size();
        Map<String, CanPackageBean> nodes = new ConcurrentHashMap(packageNum);

        Map<String, Object> params = new HashMap();
        Map<String, ItemNodes> nameKeys = new HashMap<String, ItemNodes>();
        for (Node node : packages) {
            String packageId = node.valueOf("@id");
            Number packageParamsLen = node.numberValueOf("@length");
            List<Node> itemNodes = node.selectNodes("*");
            List<ItemNodes> items = new ArrayList<ItemNodes>(itemNodes.size());
            for (Node itemNode : itemNodes) {
                ItemNodes item = this.dealItems(itemNode);
                items.add(item);
                nameKeys.put(item.getNameKey(), item);

                params.put(item.getField().toUpperCase(), null);
            }

            nodes.put(packageId, new CanPackageBean(items, packageParamsLen.intValue(), packageId));
        }
        paramsCacheProvider.put(codeName, params);
        nameKeyCacheProvider.put(codeName, nameKeys);

        return nodes;
    }

    private Map<String, AlertBean> dealAlertNodes(List<Node> packages, String codeName) {


        Map<String, AlertBean> alertMap = new ConcurrentHashMap<String, AlertBean>(packages.size());

        for (Node node : packages) {

            String key = node.selectSingleNode("key").getText();
            String function = node.selectSingleNode("function").getText();
            String description = node.selectSingleNode("description").getText();

            HashMap params = new HashMap();
            List<Node> paramNodes = node.selectSingleNode("params").selectNodes("*");
            for (Node item : paramNodes) {

                String name = item.valueOf("@name");
                Object value = item.valueOf("@value");
                params.put(name, value);
            }

            AlertBean alertBean = new AlertBean();
            alertBean.setParams(params);
            alertBean.setFunction(function.trim());
            alertBean.setDescription(description.trim());

            alertMap.put(key, alertBean);
        }

        return alertMap;
    }

    /**
     * 针对固定格式的CAN ， 包大小一致的CAN解析 包ID 包数据  （包数据长度固定）
     *
     * @param packageDatas
     * @param packageIdLen
     * @param packageLen
     * @param canCode
     * @return
     */
    public Map<String, String> parsePackage(byte[] packageDatas, int packageIdLen, int packageLen, String canCode) {
        return this.parsePackage(Unpooled.wrappedBuffer(packageDatas), packageDatas.length, packageIdLen, packageLen, canCode);

    }

    /**
     * @param bf
     * @param canCode
     * @param packageIdLen
     * @return
     */
    public Map<String, Object> parsePackage(ByteBuf bf, String canCode, int packageIdLen) {
        Map<String, CanPackageBean> packageExp = (Map<String, CanPackageBean>) canPackageProvider.get(canCode);

        Map<String, Object> fieldVals = new HashMap<String, Object>();
        if (null == packageExp) {
            logger.info("不存在功能集，软件版本号为:{}", canCode);
            return fieldVals;
        }
        if (paramsCacheProvider.containsKey(canCode)) {
            fieldVals.putAll((Map<String, Object>) paramsCacheProvider.get(canCode));
        }
        while (bf.readableBytes() > 0) {
            //判断包的长度是否异常
            if (bf.readableBytes() < packageIdLen) {
                //logger.info("解析CAN包时数据长度不够");
                return fieldVals;
            }
            byte[] packageId = new byte[packageIdLen];
            bf.readBytes(packageId);
            int packageIdInt = CommonUtils.byte2Int(packageId, packageIdLen);
            String packageIdStr = String.format("%0" + (packageIdLen << 1) + "X", packageIdInt);
            CanPackageBean bean = packageExp.get(packageIdStr);
            if (bean == null) {
                //logger.error("软件版本号[{}]，不存在CAN包ID[{}]", canCode, packageIdStr);
                continue;
                //20171222 修改应 继续解析
            }

            int packageParamsLen = bean.getPackageParamsLen();
            if (bf.readableBytes() < packageParamsLen) {
                //logger.info("解析CAN包时数据长度不够");
                return fieldVals;
            }
            byte[] packageData = new byte[packageParamsLen];
            bf.readBytes(packageData);

            Map tempFieldVals = this.parsePackageImpl(packageData, packageIdStr, bean.getPackageParams());
            fieldVals.putAll(tempFieldVals);
        }
        return fieldVals;
    }

    /**
     * @param packageData
     * @param packageId
     * @param items
     * @return
     */
    public Map<String, String> parsePackageImpl(byte[] packageData, String packageId, List<ItemNodes> items) {
        if (null == items) {
            logger.info("不存在功能集的配置:", packageId);
            return new ConcurrentHashMap();
        }

        Map<String, String> itemValues = new ConcurrentHashMap(items.size());
        for (ItemNodes item : items) {
            try {
                //空值的 不更新
                if ((this.parseItem(packageData, item).isEmpty() || this.parseItem(packageData, item) == null) && !item.isUpdateEmpty()) {
                    continue;
                }
                itemValues.put(item.getField().toUpperCase(), this.parseItem(packageData, item));
            } catch (ScriptException e) {
                logger.error("解析表达式错误：", e);
            }
        }
        return itemValues;
    }

    /**
     * @param packages
     * @return
     */
    public Map<String, Object> parsePackage(byte[] packages, String canCode, int packageIdLen) {
        return this.parsePackage(Unpooled.wrappedBuffer(packages), canCode, packageIdLen);
    }

    /**
     * 格式为 包ID 包长度  包数据
     *
     * @param packageDatas
     * @param packageIdLen
     * @param packageLen
     * @param canCode
     * @return
     */
    public Map<String, String> parsePackage1(byte[] packageDatas, int packageIdLen, int packageLen, String canCode) {
        ByteBuf bf = Unpooled.wrappedBuffer(packageDatas);
        bf.clear();
        Map<String, List<ItemNodes>> packageExp = (Map<String, List<ItemNodes>>) canPackageProvider.get(canCode);
        //包数量，通过功能集来计算 2015/8/17 不通过功能集来区can数据
        //int canNum = packageExp.size();
        Map<String, String> fieldVals = new ConcurrentHashMap<String, String>();
        while (bf.readableBytes() >= (packageIdLen + packageLen)) {
            //}
            //for (int i = 0; i < canNum; i++) {
            //判断包的长度是否异常
            if (bf.readableBytes() < (packageIdLen + packageLen)) {
                logger.info("解析CAN包时发现数据长度不够");
                return fieldVals;
            }
            byte[] packageId = new byte[packageIdLen];
            bf.readBytes(packageId);
            int packageIdInt = CommonUtils.byte2Int(packageId, packageIdLen);

            byte[] packageLenByte = new byte[packageLen];
            bf.readBytes(packageLenByte);
            int pLen = CommonUtils.byte2Int(packageLenByte, packageLen);
            if (bf.readableBytes() < pLen) {
                logger.info("解析CAN包时发现数据长度不够");
                return fieldVals;
            }
            byte[] packageData = new byte[pLen];
            bf.readBytes(packageData);

            Map tempFieldVals = this.parsePackageImpl(packageData, packageIdInt, canCode);
            fieldVals.putAll(tempFieldVals);
        }
        return fieldVals;
    }

    /**
     * 处理工作参数和can数据
     *
     * @return public Map dealPackAndParams(byte[] datas, int paramIdLen, int paramLen, String canCode) {

    ByteBuf bf = Unpooled.wrappedBuffer(datas);
    bf.clear();
    Map<String, List<ItemNodes>> paramsExp = (Map<String, List<ItemNodes>>) paramsCacheProvider.get(canCode);
    //包数量，通过功能集来计算 2015/8/17 不通过功能集来区can数据
    int paramsNum = paramsExp.size();
    Map<String, String> fieldVals = new ConcurrentHashMap<String, String>();
    for (int i = 0; i < paramsNum; i++) {
    //判断长度是否异常
    if (bf.readableBytes() < (paramIdLen + paramLen)) {
    logger.info("解析工作参数时发现数据长度不够");
    return fieldVals;
    }
    byte[] packageId = new byte[paramIdLen];
    bf.readBytes(packageId);
    int packageIdInt = CommonUtils.byte2Int(packageId, paramIdLen);

    byte[] packageLenByte = new byte[paramLen];
    bf.readBytes(packageLenByte);
    int pLen = CommonUtils.byte2Int(packageLenByte, paramLen);
    if (bf.readableBytes() < pLen) {
    logger.info("解析工作参数时发现数据长度不够");
    return fieldVals;
    }
    byte[] paramsData = new byte[pLen];
    bf.readBytes(paramsData);

    Map tempFieldVals = this.parseParamsImpl(paramsData, packageIdInt, canCode);
    fieldVals.putAll(tempFieldVals);
    }
    //如果还有数据
    if (!bf.readBoolean()) {
    logger.info("no can data!");
    return fieldVals;
    }
    //获取车型ID
    int vehType = bf.readShort();
    //获取长度
    bf.readShort();
    CanEnumConfig canConfig = CanEnumConfig.valueOf("CAN_" + vehType);
    int packageIdLen = canConfig.getPackageIdLen();
    int packageLen = canConfig.getPackageLen();
    int allLen = packageIdLen + packageLen;
    while (bf.readBoolean() && bf.readableBytes() >= allLen) {
    byte[] packageId = new byte[packageIdLen];
    bf.readBytes(packageId);
    int packageIdInt = CommonUtils.byte2Int(packageId, packageIdLen);

    byte[] packageData = new byte[packageLen];
    bf.readBytes(packageData);

    Map tempFieldVals = this.parsePackageImpl(packageData, String.format("%0" + (packageIdLen << 1) + "X", packageIdInt), canCode);
    fieldVals.putAll(tempFieldVals);
    }
    return fieldVals;
    } */

    /**
     * 解析状态位
     */
    public Map<String, String> parseStatus(byte[] statusDatas, String canCode) {
        List<ItemNodes> statusItems = (List<ItemNodes>) canStatusProvider.get(canCode);
        if (null == statusItems) {
            logger.info("不存在状态位的配置：" + canCode);
            return new ConcurrentHashMap<String, String>();
        }
        Map<String, String> itemValues = new ConcurrentHashMap<String, String>(statusItems.size());
        for (ItemNodes item : statusItems) {
            try {
                itemValues.put(item.getField().toUpperCase(), this.parseItem(statusDatas, item));
            } catch (ScriptException e) {
                logger.error("解析状态位出错：", e, item.toString());
                //e.printStackTrace();
            }
        }
        return itemValues;
    }

    /**
     * 解析can包，但是不同协议的包长度是不一定的，没有统一格式，该方法只针对统一格式
     * 统一格式为   包ID 包数据（包数据长度统一）
     *
     * @param packageDatas
     * @param allLen
     * @param packageIdLen
     * @param packageLen
     * @param canCode
     */
    public Map<String, String> parsePackage(ByteBuf packageDatas, int allLen, int packageIdLen, int packageLen, String canCode) {
        Map<String, String> fieldVals = new ConcurrentHashMap<String, String>();
        packageDatas.clear();
        //计算有共有几个包
        int packageNum = allLen / (packageIdLen + packageLen);
        for (int i = 0; i < packageNum; i++) {
            byte[] packageData = new byte[packageLen];
            byte[] packageId = new byte[packageIdLen];
            packageDatas.readBytes(packageId);
            packageDatas.readBytes(packageData);
            int packageIdInt = CommonUtils.byte2Int(packageId, packageIdLen);
            Map tempFieldVals = this.parsePackageImpl(packageData, packageIdInt, canCode);
            fieldVals.putAll(tempFieldVals);
        }
        return fieldVals;

    }

    /**
     * 解析工作参数
     *
     * @param paramsData
     * @param paramsId
     * @param canCode
     * @return public Map<String, String> parseParamsImpl(byte[] paramsData, int paramsId, String canCode) {
    Map<String, List<ItemNodes>> packageExp = (Map<String, List<ItemNodes>>) paramsCacheProvider.get(canCode);
    if (null == packageExp) {
    logger.info("不存在功能集的配置：" + canCode);
    return new ConcurrentHashMap<String, String>();
    }
    List<ItemNodes> items = packageExp.get("" + paramsId);
    if (null == items) {
    logger.info("功能集不存在工作参数配置：" + canCode + " , 参数ID为：" + Integer.toHexString(paramsId));
    return new ConcurrentHashMap<String, String>();
    }
    Map<String, String> itemValues = new ConcurrentHashMap<String, String>(items.size());
    for (ItemNodes item : items) {
    try {
    itemValues.put(item.getField().toUpperCase(), this.parseItem(paramsData, item));
    } catch (ScriptException e) {
    logger.error("解析表达式错误：", e);
    }

    }
    return itemValues;
    }*/

    /**
     * @param packageData
     * @param packageId
     * @param canCode
     */
    public Map<String, String> parsePackageImpl(byte[] packageData, int packageId, String canCode) {
        Map<String, List<ItemNodes>> packageExp = (Map<String, List<ItemNodes>>) canPackageProvider.get(canCode);
        if (null == packageExp) {
            logger.info("不存在功能集的配置：" + canCode);
            return new ConcurrentHashMap<String, String>();
        }

        List<ItemNodes> items = packageExp.get("" + packageId);
        if (null == items) {
            logger.info("功能集合不存在CAN包配置：" + canCode + " , 包ID为：" + Integer.toHexString(packageId));
//            return new ConcurrentHashMap<String, String>();
        }
        Map<String, String> itemValues = new ConcurrentHashMap<String, String>(items.size());
        for (ItemNodes item : items) {
            try {
                String val=this.parseItem(packageData, item);
                //空值的 不更新
                if ((val.isEmpty() || val == null || val.equals("")) && item.isUpdateEmpty()==false) {
                    continue;
                }
                itemValues.put(item.getField().toUpperCase(), this.parseItem(packageData, item));
            } catch (ScriptException e) {
                logger.error("解析表达式错误：", e);
            }

        }
        return itemValues;
    }

    public Map<String, String> parsePackageImpl(byte[] packageData, String packageId, String canCode) {
        Map<String, List<ItemNodes>> packageExp = (Map<String, List<ItemNodes>>) canPackageProvider.get(canCode);
        if (null == packageExp) {
            logger.info("不存在功能集的配置：" + canCode);
            return new ConcurrentHashMap<String, String>();
        }

        List<ItemNodes> items = packageExp.get(packageId);
        if (null == items) {
            logger.info("功能集合不存在CAN包配置：" + canCode + " , 包ID为：" + packageId);
            return new ConcurrentHashMap<String, String>();
        }
        Map<String, String> itemValues = new ConcurrentHashMap<String, String>(items.size());
        for (ItemNodes item : items) {
            try {
                itemValues.put(item.getField().toUpperCase(), this.parseItem(packageData, item));
            } catch (ScriptException e) {
                logger.error("解析表达式错误：", e);
            }

        }
        return itemValues;
    }

    private String parseItem(byte[] data, ItemNodes item) throws ScriptException {
        /*if(item.getField().equals("DECIMAL4")){
            System.out.println("异常数据");
        }*/
        String tVal = "";
        byte[] val = CommonUtils.byteToByte(data, item.getByteStart(), item.getByteLen(), item.getEndian());
        int tempVal = CommonUtils.byte2int(val);
        if (item.isOnlyByte()) {
            tVal = CommonUtils.parseExp(tempVal, item.getExpression(), item.getType());
        } else {
            int biteVal = CommonUtils.getBits(tempVal, item.getBitStart(), item.getBitLen());
            tVal = CommonUtils.parseExp(biteVal, item.getExpression(), item.getType());
        }
        //System.out.println("表达式为：" + item.toString());
        //System.out.println("解析出来的值为：" + tVal);
        return tVal;
    }

    /**
     * 20161207 重构工况Map 去除为空的字段
     * @param map
     * @return
     */
    public static Map reBuldMap(Map map){
         List set = new ArrayList();
        if(!map.isEmpty()){
            for(Object key :map.keySet()){
                try {
                    Object values =map.get(key);
                    if(values.equals(null)||map.get(key).equals("null")){
                        set.add(key);
                    }
                } catch (Exception e) {
                    set.add(key);
                }
            }
        }

        if(!set.isEmpty()){
            for(Object del:set){
                map.remove(del);
            }
        }

        return map;
    }
}
