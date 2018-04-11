package com.tiza.gw.support.task.timer;

import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.dao.VehicleInfoDao;
import com.tiza.gw.support.entity.VehicleInfoEntity;
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
 * Description: VehicleInfoTask
 * Author: DIYILIU
 * Update: 2018-04-10 16:33
 */

@Slf4j
@Service
public class VehicleInfoTask implements ITask {

    @Resource(name = "vehicleInfoDaoImpl")
    private VehicleInfoDao vehicleInfoDao;

    @Resource(name = "vehicleInfoCacheProvider")
    private ICache cacheProvider;

    @Override
    public void execute() {
        log.info("刷新车辆列表...");
        List res = vehicleInfoDao.selectVehicle();
        this.dealResult(res);
    }

    private void dealResult(List<VehicleInfoEntity> res) {
        Set keys = cacheProvider.getKeys();
        Set temp = new HashSet(res.size());
        if (null == res || 0 == res.size()) {
            log.info("VehicleInfoTask 任务，获取车辆信息为空");
            return;
        }

        for (VehicleInfoEntity entity : res) {
            cacheProvider.put(entity.getVinCode(), entity);
            temp.add(entity.getVinCode());
        }

        Collection<String> subKeys = CollectionUtils.subtract(keys, temp);
        for (String tempKey : subKeys) {
            cacheProvider.remove(tempKey);
        }
    }
}
