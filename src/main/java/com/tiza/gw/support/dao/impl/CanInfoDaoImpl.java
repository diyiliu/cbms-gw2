package com.tiza.gw.support.dao.impl;

import com.tiza.gw.support.dao.BaseDao;
import com.tiza.gw.support.dao.CanInfoDao;
import com.tiza.gw.support.entity.CanInfoEntity;
import com.tiza.gw.support.other.SqlXmlCache;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-17 14:22)
 * Version: 1.0
 * Updated:
 */
@Repository
@Transactional
public class CanInfoDaoImpl extends BaseDao implements CanInfoDao {
    @Transactional(readOnly = true)
    @Override
    public List<Map<String, Object>> selectVehicle(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CanInfoEntity> selectVehicle() {
        //select CODE , NAME , XML from BS_FUNCTIONSET t
        String selectSql = SqlXmlCache.getSql("selectCan");
        return jdbcTemplate.query(selectSql, new RowMapper<CanInfoEntity>() {
            @Override
            public CanInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                CanInfoEntity entity = new CanInfoEntity();
                entity.setSoftVersionCode(rs.getString("softVersionCode"));
                entity.setFunctionName(rs.getString("functionName"));
                entity.setXml(rs.getString("xml"));
                entity.setAlertXml(rs.getString("alertXml"));

                return entity;
            }
        });
    }
}
