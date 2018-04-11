package com.tiza.gw.support.dao;

import com.tiza.gw.support.entity.VehicleInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-10-28 09:29)
 * Version: 1.0
 * Updated:
 */
public interface VehicleInfoDao {
    public List<Map<String, Object>> selectVehicle(String sql);

    public List<VehicleInfoEntity> selectVehicle();

}
