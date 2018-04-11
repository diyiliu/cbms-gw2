package com.tiza.gw.support.task.timer;

import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.dao.AlarmInfoDao;
import com.tiza.gw.support.entity.AlarmInfoEntity;
import com.tiza.gw.support.task.ITask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: AlarmInfoTask
 * Author: DIYILIU
 * Update: 2018-04-10 16:39
 */

@Slf4j
@Service
public class AlarmInfoTask implements ITask {

    @Resource(name = "alarmInfoDaoImpl")
    private AlarmInfoDao dao;

    @Resource(name = "alarmInfoCacheProvider")
    private ICache cacheProvider;

    @Resource(name = "fenceAlarmInfoCacheProvider")
    private ICache fenceAlarmInfoCacheProvider;

    @Resource(name = "workAlarmInfoCacheProvider")
    private ICache workAlarmInfoCacheProvider;

    @Override
    public void execute() {
        log.info("刷新报警信息...");
        List<AlarmInfoEntity> res = dao.selectVehicleAlarm();
        this.dealResult(res);
    }

    private void dealResult(List<AlarmInfoEntity> res) {
        Set keys = cacheProvider.getKeys();
        Set fenceKeys = fenceAlarmInfoCacheProvider.getKeys();
        Set workKeys = workAlarmInfoCacheProvider.getKeys();
        Set temp = new HashSet(res.size());
        Set fenceTemp = new HashSet();
        Set workTemp = new HashSet();

        if (null == res || 0 == res.size()) {
            log.info("AlarmInfoTask 任务，获取信息为空");
            return;
        }
        for (AlarmInfoEntity entity : res) {
            String vehicleId = String.valueOf(entity.getVehicleId());
            cacheProvider.put(vehicleId, entity);
            if (entity.getAlarmCategory() == 2) {
                fenceAlarmInfoCacheProvider.put(vehicleId, entity);
                fenceTemp.add(vehicleId);

            }else if (entity.getAlarmCategory() == 3){
                workAlarmInfoCacheProvider.put(vehicleId + entity.getNameKey(), entity);
                workTemp.add(vehicleId + entity.getNameKey());
            }
            temp.add(vehicleId);
        }
        Collection<String> subKeys = CollectionUtils.subtract(keys, temp);
        for (String tempKey : subKeys) {
            cacheProvider.remove(tempKey);
        }

        Collection<String> subFenceKeys = CollectionUtils.subtract(fenceKeys, fenceTemp);
        for (String tempKey : subFenceKeys) {
            fenceAlarmInfoCacheProvider.remove(tempKey);
        }

        Collection<String> subWorkKeys = CollectionUtils.subtract(workKeys, workTemp);
        for (String tempKey : subWorkKeys) {
            workAlarmInfoCacheProvider.remove(tempKey);
        }
    }
}
