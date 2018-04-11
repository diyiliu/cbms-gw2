package com.tiza.gw.support.other;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.Map;

public final class SqlXmlCache {
    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(SqlXmlCache.class);

    /**
     * 记录所有SQL语句的xmlName
     */
    private final String xmlName = "SQL.xml";

    /**
     * 记录所有SQL语句的XML信息
     */
    private static Map<String, String> sqlXmlMap = new LinkedCaseInsensitiveMap<String>();

    private static Map<String, String> FIELD_TYPE = new LinkedCaseInsensitiveMap<String>();

    /**
     * 读取记录所有SQL语句的XML信息，存入缓存
     *
     * @see [类、类#方法、类#成员]
     */
    public void readSqlXmlCache() {
        sqlXmlMap.clear();
        FIELD_TYPE.clear();
        logger.info("启动，读取记录所有SQL语句的XML信息！");
        try {
            InputStream is = new ClassPathResource(xmlName).getInputStream();
            DocumentBuilder domBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = domBuilder.parse(is);
            Element root = doc.getDocumentElement();
            NodeList sqlNodes = root.getElementsByTagName("sql");
            if (sqlNodes != null) {
                for (int i = 0; i < sqlNodes.getLength(); i++) {
                    Node nodeItem = sqlNodes.item(i);
                    Node idAttrNode = nodeItem.getAttributes().getNamedItem("id");
                    Node descAttrNode = nodeItem.getAttributes().getNamedItem("description");
                    Node fieldTypeNode = nodeItem.getAttributes().getNamedItem("fieldType");
                    if (idAttrNode == null) {
                        Exception e = new RuntimeException("解析XML失败，节点SQL上缺少id属性,节点顺序为" + (i + 1));
                        logger.error("parse SqlNode Error", e);
                        continue;
                    }
                    String descNodeValue = descAttrNode.getNodeValue();
                    String idNodeValue = idAttrNode.getNodeValue();
                    String fieldTypeValue = fieldTypeNode.getNodeValue();
                    String sqlText = nodeItem.getTextContent().trim();
                    logger.info("KEY:" + idNodeValue + " DESCRIPTION: " + descNodeValue + " SQL SIZE IS " + sqlText.length());
                    sqlXmlMap.put(idNodeValue, StringUtils.trim(sqlText));

                    FIELD_TYPE.put(idNodeValue, fieldTypeValue);
                }
            }
        } catch (Exception e) {
            logger.error("ERROR:读取SQL文件信息出错", e);
        } finally {
            logger.info("成功读取记录所有SQL语句的XML信息！");
        }
    }

    /**
     * 获取记录所有SQL语句的XML信息
     * return Map<String, String> 记录所有SQL语句的XML信息
     *
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getSqlXmlCache() {
        return sqlXmlMap;

    }

    public void Init() throws RuntimeException {
        this.readSqlXmlCache();
    }

    public Object getParam(Object key) {
        return sqlXmlMap.get(key);
    }

    public static String getSql(String key) {
        return sqlXmlMap.get(key);
    }

    public static String getField(String key) {
        return FIELD_TYPE.get(key);
    }
}
