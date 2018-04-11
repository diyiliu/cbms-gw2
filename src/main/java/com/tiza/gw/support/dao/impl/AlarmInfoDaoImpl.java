package com.tiza.gw.support.dao.impl;

import com.tiza.gw.support.dao.AlarmInfoDao;
import com.tiza.gw.support.dao.BaseDao;
import com.tiza.gw.support.entity.AlarmInfoEntity;
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
 * Created:Wolf-(2014-12-20 14:04)
 * Version: 1.0
 * Updated:
 */
@Repository
@Transactional
public class AlarmInfoDaoImpl extends BaseDao implements AlarmInfoDao {
    @Transactional(readOnly = true)
    @Override
    public List<Map<String, Object>> selectVehicleAlarm(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlarmInfoEntity> selectVehicleAlarm() {
        /*id,
               vehicleid,
               alarmcategory,
               starttime,
               endtime,
               startlng ,
               startlat,
               endlng,
               endlat,
               fenceid*/
        String selectSql = SqlXmlCache.getSql("selectAlarm");
        return jdbcTemplate.query(selectSql, new RowMapper<AlarmInfoEntity>() {
            @Override
            public AlarmInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                AlarmInfoEntity entity = new AlarmInfoEntity();
                entity.setId(rs.getInt(1));
                entity.setVehicleId(rs.getInt(2));
                entity.setAlarmCategory(rs.getInt(3));
                entity.setStartTime(rs.getDate(4));
                entity.setEndTime(rs.getDate(5));
                entity.setStartLng(rs.getDouble(6));
                entity.setStartLat(rs.getDouble(7));
                entity.setEndLng(rs.getDouble(8));
                entity.setEndLat(rs.getDouble(9));
                entity.setFenceId(rs.getInt(10));
                entity.setNameKey(rs.getString(11));
                return entity;
            }
        });
    }

    @Transactional(readOnly = true)
    @Override
    public AlarmInfoEntity selectVehicleAlarmById(int vehicleId) {
        AlarmInfoEntity result = null;
        try {
            String selectSql = SqlXmlCache.getSql("selectAlarmById");
            result = jdbcTemplate.queryForObject(selectSql, new Object[]{vehicleId}, new RowMapper<AlarmInfoEntity>() {
                @Override
                public AlarmInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AlarmInfoEntity entity = new AlarmInfoEntity();
                    entity.setId(rs.getInt(1));
                    entity.setVehicleId(rs.getInt(2));
                    entity.setAlarmCategory(rs.getInt(3));
                    entity.setStartTime(rs.getDate(4));
                    entity.setEndTime(rs.getDate(5));
                    entity.setStartLng(rs.getDouble(6));
                    entity.setStartLat(rs.getDouble(7));
                    entity.setEndLng(rs.getDouble(8));
                    entity.setEndLat(rs.getDouble(9));
                    entity.setFenceId(rs.getInt(10));
                    return entity;
                }
            });
        } catch (Exception e) {
            logger.error("selectVehicleAlarmById error:", e);
        }
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlarmInfoEntity> selectFenceAlarm() {
        String selectSql = SqlXmlCache.getSql("selectFenceAlarm");
        return jdbcTemplate.query(selectSql, new RowMapper<AlarmInfoEntity>() {
            @Override
            public AlarmInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                AlarmInfoEntity entity = new AlarmInfoEntity();
                entity.setId(rs.getInt(1));
                entity.setVehicleId(rs.getInt(2));
                entity.setAlarmCategory(rs.getInt(3));
                entity.setStartTime(rs.getDate(4));
                entity.setEndTime(rs.getDate(5));
                entity.setStartLng(rs.getDouble(6));
                entity.setStartLat(rs.getDouble(7));
                entity.setEndLng(rs.getDouble(8));
                entity.setEndLat(rs.getDouble(9));
                entity.setFenceId(rs.getInt(10));
                return entity;
            }
        });
    }
}
