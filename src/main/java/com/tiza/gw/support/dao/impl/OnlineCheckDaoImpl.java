package com.tiza.gw.support.dao.impl;

import com.tiza.gw.support.dao.BaseDao;
import com.tiza.gw.support.dao.OnlineCheckDao;
import com.tiza.gw.support.entity.OnlineEntity;
import com.tiza.gw.support.other.SqlXmlCache;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by admin on 2017/4/26.
 */
@Repository
@Transactional
public class OnlineCheckDaoImpl extends BaseDao implements OnlineCheckDao {
    @Transactional(readOnly = true)
    @Override
    public List<OnlineEntity> selectOnline() {
        String selectSql = SqlXmlCache.getSql("selectOnline");
        return jdbcTemplate.query(selectSql, new RowMapper<OnlineEntity>() {
            @Override
            public OnlineEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                OnlineEntity entity = new OnlineEntity();
                entity.setVehicleId(rs.getInt("vehicleId"));
                entity.setCreateTime(rs.getTimestamp("gpstime"));
                entity.setAccStatus(rs.getInt("accstatus"));
                return entity;
            }
        });
    }
}
