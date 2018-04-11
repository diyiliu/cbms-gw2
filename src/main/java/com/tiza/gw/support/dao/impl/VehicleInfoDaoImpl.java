package com.tiza.gw.support.dao.impl;

import com.tiza.gw.support.dao.BaseDao;
import com.tiza.gw.support.dao.VehicleInfoDao;
import com.tiza.gw.support.entity.VehicleInfoEntity;
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
 * Created:Wolf-(2014-10-28 09:29)
 * Version: 1.0
 * Updated:
 */
@Repository
@Transactional
public class VehicleInfoDaoImpl extends BaseDao implements VehicleInfoDao {

    @Transactional(readOnly = true)
    @Override
    public List<Map<String, Object>> selectVehicle(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
    @Transactional(readOnly = true)
    @Override
    public List<VehicleInfoEntity> selectVehicle() {
        // select b.ID as VEHICLEID , a.DEVICEID , a.TERMINALNO ,a.SIMNO ,a.PROTOCOL ,a.DCSFUNCTIONSETCODE ,a.LOCKFUNCTIONSETCODE ,a.MODEL  from bs_terminal a left join bs_vehicle b on a.deviceid=b.deviceid where a.protocol=1
        String selectSql = SqlXmlCache.getSql("selectVehicle");
        return jdbcTemplate.query(selectSql, new RowMapper<VehicleInfoEntity>() {
            @Override
            public VehicleInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                VehicleInfoEntity entity = new VehicleInfoEntity();
                entity.setVehicleId(rs.getInt("vehicleId"));
                entity.setCreateTime(rs.getTimestamp("createtime"));
                entity.setGpsInstallStatus(rs.getInt("gpsinstallstatus"));
                entity.setInnerCode(rs.getString("innercode"));
                entity.setVinCode(rs.getString("vincode"));
                entity.setOwnerUserId(rs.getInt("owneruserid"));
                entity.setProductTypeId(rs.getInt("producttypeid"));
                entity.setProductModel(rs.getString("productmodel"));
                entity.setUseStatus(rs.getInt("usestatus"));
                return entity;
            }
        });
    }
}
