package com.tiza.gw.support.dao;

import com.tiza.gw.support.entity.AlarmInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-20 14:03)
 * Version: 1.0
 * Updated:
 */
public interface AlarmInfoDao {
    public List<Map<String, Object>> selectVehicleAlarm(String sql);
    public List<AlarmInfoEntity> selectVehicleAlarm();
    public AlarmInfoEntity selectVehicleAlarmById(int vehicleId);
    public List<AlarmInfoEntity> selectFenceAlarm();
}
