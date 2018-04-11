package com.tiza.gw.support.dao.impl;

import com.tiza.gw.support.dao.BaseDao;
import com.tiza.gw.support.dao.VehicleGpsInfoDao;
import com.tiza.gw.support.other.SqlXmlCache;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-03-03 15:35)
 * Version: 1.0
 * Updated:
 */
@Repository
@Transactional
public class VehicleGpsInfoDaoImpl extends BaseDao implements VehicleGpsInfoDao {
    @Transactional
    @Override
    public boolean updateStatus() {
        String sql = SqlXmlCache.getSql("updateVehicleStatus");
        jdbcTemplate.update(sql);
        return true;
    }

}
