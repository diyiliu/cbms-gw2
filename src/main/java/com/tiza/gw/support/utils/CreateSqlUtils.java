package com.tiza.gw.support.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-10-27 17:32)
 * Version: 1.0
 * Updated:
 */
public class CreateSqlUtils {
    //表名
    private String tableName;
    //表用户
    private String tableUser;
    //正则
    private String rep;

    private Map values;

    private Map whereCase;

    private StringBuffer sql;

    private int sqlType; // 1 insert 2 update

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    private CreateSqlUtils setSql() {
        this.sql = new StringBuffer();
        return this;
    }

    public Map getValues() {
        return values;
    }

    public void setValues(Map values) {
        this.values = values;
    }

    public Map getWhereCase() {
        return whereCase;
    }

    public void setWhereCase(Map whereCase) {
        this.whereCase = whereCase;
    }

    public String getSql() {
        return this.sql.toString();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableUser() {
        return tableUser;
    }

    public void setTableUser(String tableUser) {
        this.tableUser = tableUser;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public String addParams() {
        return null;
    }

    /**
     *
     */
    public void createSql() {
        this.setSql().addSqlHead().addFieldAndValues();
        switch (this.sqlType) {
            case 1:
                this.setSql().addSqlHead().addFieldAndValues();
                break;
            case 2:
                this.setSql().addSqlHead().addFieldAndValues().addWhereCase();
                break;
            default:
                break;
        }
    }


    private CreateSqlUtils addSqlHead() {
        switch (this.sqlType) {
            case 1:
                this.addInsertHead();
                break;
            case 2:
                this.addUpdateHead();
                break;
            default:
                break;
        }

        return this;
    }

    private void addInsertHead() {
        this.sql.append("insert into ");
        if (StringUtils.isNotEmpty(this.tableUser)) {
            this.sql.append(this.tableUser).append(".");
        }
        this.sql.append(this.tableName);
    }

    private void addUpdateHead() {
        this.sql.append("update ");
        if (StringUtils.isNotEmpty(this.tableUser)) {
            this.sql.append(this.tableUser).append(".");
        }
        this.sql.append(this.tableName).append(" set ");
    }


    private CreateSqlUtils insertFieldAndValues() {
        /*Iterator keys = this.values.keySet().iterator();*/
        this.sql.append(" (");
        int size = this.values.size();
        int i = 1;
        /*while (keys.hasNext()){
            String field = keys.next().toString();
            if(i == size){
                this.sql.append(field);
            }else{
                this.sql.append(field).append(" , ");
            }
            i++;
        }*/
        Set<Map.Entry<String, Object>> values = this.values.entrySet();
        StringBuffer tempKeys = new StringBuffer();
        StringBuffer tempValues = new StringBuffer();
        Iterator iterator = values.iterator();
        while (iterator.hasNext()) {
            Map.Entry res = (Map.Entry<String, Object>) iterator.next();
            if (i == size) {
                tempKeys.append(res.getKey());

                this.formateValue(tempValues, res.getValue());
            } else {
                tempKeys.append(res.getKey()).append(" , ");

                this.formateValue(tempValues, res.getValue());
                tempValues.append(" , ");
            }
            i++;
        }
        this.sql.append(tempKeys.toString()).append(" ) values ").append("(").append(tempValues.toString()).append(" ) ");
        return this;
    }

    private CreateSqlUtils updateFieldAndValues() {

        int size = this.values.size();
        int i = 1;
        /*while (keys.hasNext()){
            String field = keys.next().toString();
            if(i == size){
                this.sql.append(field);
            }else{
                this.sql.append(field).append(" , ");
            }
            i++;
        }*/
        Set<Map.Entry<String, Object>> values = this.values.entrySet();
        StringBuffer tempSet = new StringBuffer();
        Iterator iterator = values.iterator();
        while (iterator.hasNext()) {
            Map.Entry res = (Map.Entry<String, Object>) iterator.next();
            if (i == size) {
                tempSet.append(res.getKey()).append("=");
                this.formateValue(tempSet, res.getValue());
            } else {
                tempSet.append(res.getKey()).append("=");
                this.formateValue(tempSet, res.getValue());
                tempSet.append(" , ");
            }
            i++;
        }
        this.sql.append(tempSet.toString());
        return this;

    }

    private CreateSqlUtils addFieldAndValues() {
        switch (this.sqlType) {
            case 1:
                this.insertFieldAndValues();
                break;
            case 2:
                this.updateFieldAndValues();
                break;
            default:
                break;
        }
        return this;
    }

    private CreateSqlUtils addWhereCase() {
        if (this.whereCase == null) {
            return this;
        }
        this.sql.append(" where ");
        int size = this.whereCase.size();
        int i = 1;
        Set<Map.Entry<String, Object>> values = this.whereCase.entrySet();
        StringBuffer tempWhere = new StringBuffer();
        Iterator iterator = values.iterator();
        while (iterator.hasNext()) {
            Map.Entry res = (Map.Entry<String, Object>) iterator.next();
            if (i == size) {
                tempWhere.append(res.getKey()).append("=");
                this.formateValue(tempWhere, res.getValue());
            } else {
                tempWhere.append(res.getKey()).append("=");
                this.formateValue(tempWhere, res.getValue());
                tempWhere.append(" and ");
            }
            i++;
        }
        this.sql.append(tempWhere.toString());

        return this;
    }

    private void formateValue(StringBuffer sb, Object value) {
        if (value instanceof String) {
            sb.append("'").append(value).append("'");
        } else if (value instanceof Number) {
            sb.append(value);
        } else if (value instanceof Date) {
            sb.append("to_date('").append(DateUtils.formatDate((Date) value, DateUtils.All_DAY_FORMAT)).append("' , 'yyyy-mm-dd hh24:mi:ss')");
        } else if (value == null) {
            sb.append(value);
        }

    }

}
