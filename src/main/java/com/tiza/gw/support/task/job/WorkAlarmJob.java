package com.tiza.gw.support.task.job;

import com.tiza.gw.support.bean.AlertBean;
import com.tiza.gw.support.bean.ItemNodes;
import com.tiza.gw.support.bean.WorkAlarmBean;
import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.entity.AlarmInfoEntity;
import com.tiza.gw.support.task.ITask;
import com.tiza.gw.support.utils.CommonUtils;
import com.tiza.gw.support.utils.DateUtils;
import com.tiza.gw.support.utils.EnumConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.script.ScriptException;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: WorkAlarmJob
 * Author: DIYILIU
 * Update: 2018-04-10 17:09
 */

@Slf4j
@Service
public class WorkAlarmJob implements ITask {
    private static Queue<WorkAlarmBean> dataPool = new ConcurrentLinkedQueue();

    @Resource
    private ICache alertItemCacheProvider;

    @Resource
    private ICache nameKeyCacheProvider;

    @Resource
    private ICache workAlarmInfoCacheProvider;

    @Override
    public void execute() {
        while (!dataPool.isEmpty()){
            WorkAlarmBean workAlarmBean = dataPool.poll();
            String softVersion = workAlarmBean.getSoftVersion();

            if (!alertItemCacheProvider.containsKey(softVersion) || !nameKeyCacheProvider.containsKey(softVersion)) {
                continue;
            }

            Map<String, AlertBean> alertItem = (Map<String, AlertBean>) alertItemCacheProvider.get(softVersion);
            Map<String, ItemNodes> nameKeys = (Map<String, ItemNodes>) nameKeyCacheProvider.get(softVersion);

            Set<Map.Entry<String, AlertBean>> alertSet = alertItem.entrySet();
            try {
                for (Iterator<Map.Entry<String, AlertBean>> iterator = alertSet.iterator(); iterator.hasNext(); ) {
                    Map.Entry<String, AlertBean> entry = iterator.next();
                    String alertKey = entry.getKey();
                    AlertBean alertBean = entry.getValue();
                    if (setValue(nameKeys, alertBean, workAlarmBean.getItemValue())) {
                        dealAlarm(workAlarmBean, alertKey, alertBean.getDescription(), alertBean.doAlert());
                    }
                }
            } catch (ScriptException e) {
                log.error("处理工况报警错误！{}", e);
            }
        }
    }

    private boolean setValue(Map<String, ItemNodes> nameKeys, AlertBean alertBean, Map values) {

        Map params = alertBean.getParams();
        for (Iterator<Map.Entry> iterator = params.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = iterator.next();
            String key = (String) entry.getKey();
            if (nameKeys.containsKey(key)) {
                String field = nameKeys.get(key).getField();
                Object value = values.get(field.toUpperCase());
                if (value == null) {
                    return false;
                }
                params.put(key, value);
            }
        }
        alertBean.setParams(params);

        return true;
    }

    private void dealAlarm(final WorkAlarmBean alarmBean, final String alertKey, final String description, boolean isAlarm) {

        if (workAlarmInfoCacheProvider.containsKey(alarmBean.getVehicleId() + alertKey)) {

            // 取消报警
            if (!isAlarm) {
                //update 操作
                StringBuffer strb = new StringBuffer("update ")
                        .append(EnumConfig.DbConfig.DB_USER_CBMS)
                        .append(".")
                        .append(EnumConfig.DbConfig.VEHICLE_ALARM)
                        .append(" set ENDTIME=").append("to_date('")
                        .append(DateUtils.formatDate(alarmBean.getGpsTime(), DateUtils.All_DAY_FORMAT))
                        .append("' , 'yyyy-mm-dd hh24-mi-ss')")
                        .append(",ENDLNG=")
                        .append(alarmBean.getLng())
                        .append(",ENDLAT=")
                        .append(alarmBean.getLat())
                        .append(" where ENDTIME is null")
                        .append(" and vehicleid=").append(alarmBean.getVehicleId())
                        .append(" and alarmkey='").append(alertKey).append("'");

                CommonUtils.dealInfoTODb(strb.toString());
                workAlarmInfoCacheProvider.remove(alarmBean.getVehicleId() + alertKey);
            }
        } else {
            // 开始报警
            if (isAlarm) {
                Map insertMap = new ConcurrentHashMap();
                insertMap.put("VEHICLEID", alarmBean.getVehicleId());
                insertMap.put("ALARMCATEGORY", 3);
                insertMap.put("STARTLNG", alarmBean.getLng());
                insertMap.put("STARTTIME", alarmBean.getGpsTime());
                insertMap.put("STARTLAT", alarmBean.getLat());
                insertMap.put("DETAIL", description);
                insertMap.put("ALARMKEY", alertKey);

                CommonUtils.dealInfoTODb(EnumConfig.DbConfig.VEHICLE_ALARM, EnumConfig.DbConfig.DB_USER_CBMS, insertMap);
                workAlarmInfoCacheProvider.put(alarmBean.getVehicleId() + alertKey, new AlarmInfoEntity(alarmBean.getVehicleId(), 3,
                        alarmBean.getGpsTime(), alarmBean.getLng(), alarmBean.getLat(), alertKey));
            }
        }
    }


    public static void sendData(WorkAlarmBean data) {

        dataPool.add(data);
    }
}
