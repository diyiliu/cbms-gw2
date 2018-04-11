package com.tiza.gw.support.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-04-17 14:24)
 * Version: 1.0
 * Updated:
 */
public abstract class BaseDao<T> {
    protected Logger logger = LoggerFactory
            .getLogger(this.getClass());

    private static final String TABLE_NAME = "#TABLE_NAME#";

    @Resource(name = "jdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    /*@Transactional
    public void batchInsert(Collection<String> sqls) {
        sqls.toArray();
        jdbcTemplate.batchUpdate(sqls.toArray());
    }*/

    public String getTableName(String user, String tablePix) {
        //获取当前的年月
        String suff = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMM");

        return user + "." + tablePix + suff;

    }

    public String getTableName(String user, String tablePix, Date date) {
        //获取当前的年月
        String suff = DateFormatUtils.format(date.getTime(), "yyyyMM");

        return user + "." + tablePix + suff;

    }

    public String getTableName(String tablePix) {
        //获取当前的年月
        String suff = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
        return tablePix + suff;
    }

    public String getSql(String sql, String tablePix, String formate) {
        if (StringUtils.contains(sql, TABLE_NAME)) {
            String realTableName = tablePix + DateFormatUtils.format(System.currentTimeMillis(), formate);
            return StringUtils.replace(sql, TABLE_NAME, realTableName, 1);
        }
        return sql;
    }
}
