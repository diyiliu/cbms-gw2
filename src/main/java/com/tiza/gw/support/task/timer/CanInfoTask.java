package com.tiza.gw.support.task.timer;

import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.dao.CanInfoDao;
import com.tiza.gw.support.entity.CanInfoEntity;
import com.tiza.gw.support.task.ITask;
import com.tiza.gw.support.utils.CanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: CanInfoTask
 * Author: DIYILIU
 * Update: 2018-04-10 16:36
 */

@Slf4j
@Service
public class CanInfoTask implements ITask {

    @Resource(name = "canInfoDaoImpl")
    private CanInfoDao dao;

    @Resource(name = "canInfoCacheProvider")
    private ICache cacheProvider;

    @Resource
    private CanUtils canUtils;

    @Resource(name = "canPackageProvider")
    private ICache canPackageProvider;

    @Resource(name = "canStatusProvider")
    private ICache canStatusProvider;

    @Resource(name = "paramsCacheProvider")
    private ICache paramsCacheProvider;

    @Resource(name = "nameKeyCacheProvider")
    private ICache nameKeyCacheProvider;

    @Resource(name = "alertItemCacheProvider")
    private ICache alertItemCacheProvider;

    @Override
    public void execute() {
        log.info("刷新功能集信息...");
        List res = dao.selectVehicle();
        this.dealResult(res);
    }


    private void dealResult(List<CanInfoEntity> res) {
        Set keys = cacheProvider.getKeys();
        Set temp = new HashSet(res.size());

        Set packageKeys = canPackageProvider.getKeys();
        Set packageTemp = new HashSet();

        Set statusKeys = canStatusProvider.getKeys();
        Set statusTemp = new HashSet();

        Set paramsKeys = paramsCacheProvider.getKeys();
        Set paramsTemp = new HashSet();

        Set nameKeyKeys = nameKeyCacheProvider.getKeys();
        Set nameKeyTemp = new HashSet();

        Set alertItemKeys = alertItemCacheProvider.getKeys();
        Set alertItemTemp = new HashSet();

        if (null == res || 0 == res.size()) {
            log.info("CanInfoTask 任务，获取功能集为空");
            return;
        }
        for (CanInfoEntity entity : res) {
            cacheProvider.put(entity.getSoftVersionCode(), entity);
            temp.add(entity.getSoftVersionCode());
            statusTemp.add(entity.getSoftVersionCode());
            packageTemp.add(entity.getSoftVersionCode());
            paramsTemp.add(entity.getSoftVersionCode());
            nameKeyTemp.add(entity.getSoftVersionCode());
            alertItemTemp.add(entity.getSoftVersionCode());
            //解析CAN
            canUtils.dealCan(entity);
        }

        Collection<String> subKeys = CollectionUtils.subtract(keys, temp);
        for (String tempKey : subKeys) {
            cacheProvider.remove(tempKey);
        }

        Collection<String> subPackageKeys = CollectionUtils.subtract(packageKeys, packageTemp);
        for (String tempKey : subPackageKeys) {
            canPackageProvider.remove(tempKey);
        }
        Collection<String> subStatusKeys = CollectionUtils.subtract(statusKeys, statusTemp);
        for (String tempKey : subStatusKeys) {
            canStatusProvider.remove(tempKey);
        }

        Collection<String> subParamsKeys = CollectionUtils.subtract(paramsKeys, paramsTemp);
        for (String tempKey : subParamsKeys) {
            paramsCacheProvider.remove(tempKey);
        }

        Collection<String> subNameKeyKeys = CollectionUtils.subtract(nameKeyKeys, nameKeyTemp);
        for (String tempKey : subNameKeyKeys) {
            nameKeyCacheProvider.remove(tempKey);
        }

        Collection<String> subAlertItemKeys = CollectionUtils.subtract(alertItemKeys, alertItemTemp);
        for (String tempKey : subAlertItemKeys) {
            alertItemCacheProvider.remove(tempKey);
        }
    }
}
