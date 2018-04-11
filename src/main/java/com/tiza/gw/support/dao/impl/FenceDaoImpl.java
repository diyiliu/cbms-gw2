package com.tiza.gw.support.dao.impl;

import com.tiza.gw.support.dao.BaseDao;
import com.tiza.gw.support.dao.FenceDao;
import com.tiza.gw.support.entity.FenceEntity;
import com.tiza.gw.support.other.SqlXmlCache;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-10-26 15:19)
 * Version: 1.0
 * Updated:
 */
@Repository
@Transactional
public class FenceDaoImpl extends BaseDao implements FenceDao {
    @Transactional(readOnly = true)
    @Override
    public List<FenceEntity> getAll() {
        /*t.id,
                t.vehicleid,
                t.name,
                t.sharp,
                t.geoinfo,
                t.alarmtype,
                t.status*/
        String selectSql = SqlXmlCache.getSql("selectFence");
        return jdbcTemplate.query(selectSql, new RowMapper<FenceEntity>() {
            @Override
            public FenceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                FenceEntity entity = new FenceEntity();
                entity.setId(rs.getInt(1));
                entity.setVehicleId(rs.getInt(2));
                entity.setName(rs.getString(3));
                entity.setSharp(rs.getInt(4));
                entity.setGeoInfo(rs.getString(5));
                entity.setAlarmType(rs.getInt(6));
                entity.setStatus(rs.getInt(7));
                return entity;
            }
        });
    }
}
