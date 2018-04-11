package com.tiza.gw.support.task.timer;

import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.dao.FenceDao;
import com.tiza.gw.support.entity.FenceEntity;
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
 * Description: FenceInfoTask
 * Author: DIYILIU
 * Update: 2018-04-10 16:44
 */

@Slf4j
@Service
public class FenceInfoTask implements ITask {

    @Resource(name = "fenceDaoImpl")
    private FenceDao dao;

    @Resource(name = "fenceCacheProvider")
    private ICache cacheProvider;

    @Override
    public void execute() {
        log.info("刷新围栏信息...");
        List<FenceEntity> res = dao.getAll();
        this.dealResult(res);
    }


    private void dealResult(List<FenceEntity> res) {
        Set keys = cacheProvider.getKeys();
        Set temp = new HashSet(res.size());
        if (null == res || 0 == res.size()) {
            log.info("FenceInfoTask 任务，获取信息为空");
            return;
        }
        for (FenceEntity entity : res) {
            cacheProvider.put(String.valueOf(entity.getVehicleId()), entity);
            temp.add(String.valueOf(entity.getVehicleId()));
        }
        Collection<String> subKeys = CollectionUtils.subtract(keys, temp);
        for (String tempKey : subKeys) {
            cacheProvider.remove(tempKey);
        }
    }
}
