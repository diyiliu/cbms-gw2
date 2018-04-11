package com.tiza.gw.support.config;

import com.tiza.gw.support.task.job.FenceAlarmJob;
import com.tiza.gw.support.task.job.WorkAlarmJob;
import com.tiza.gw.support.task.timer.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * Description: SpringQuartz
 * Author: DIYILIU
 * Update: 2018-04-04 14:30
 */

@Configuration
@EnableScheduling
public class SpringQuartz {

    @Resource
    private FenceAlarmJob fenceAlarmJob;

    @Resource
    private WorkAlarmJob workAlarmJob;

    @Resource
    private VehicleInfoTask vehicleInfoTask;

    @Resource
    private CanInfoTask canInfoTask;

    @Resource
    private FenceInfoTask fenceInfoTask;

    @Resource
    private AlarmInfoTask alarmInfoTask;

    @Resource
    private OnlineCheckTask onlineCheckTask;

    /**
     * 围栏报警任务
     */
    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 1 * 1000)
    public void fenceAlarmJob() {

        fenceAlarmJob.execute();
    }


    /**
     * 工况报警任务
     */
    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 3 * 1000)
    public void workAlarmJob() {

        workAlarmJob.execute();
    }


    /**
     * 刷新车辆列表
     */
    @Scheduled(fixedDelay = 15 * 60 * 1000, initialDelay = 5 * 1000)
    public void refreshVehicleInfoTask() {

        vehicleInfoTask.execute();
    }


    /**
     * 刷新功能集列表
     */
    @Scheduled(fixedDelay = 30 * 60 * 1000, initialDelay = 10 * 1000)
    public void refreshCanInfoTask() {

        canInfoTask.execute();
    }

    /**
     * 刷新围栏列表
     */
    @Scheduled(fixedDelay = 30 * 60 * 1000, initialDelay = 15 * 1000)
    public void refreshFenceInfoTask() {

        fenceInfoTask.execute();
    }

    /**
     * 刷新报警信息
     */
    @Scheduled(fixedDelay = 15 * 60 * 1000, initialDelay = 20 * 1000)
    public void refreshAlarmInfoTask() {

        alarmInfoTask.execute();
    }

    /**
     * 车辆在线检测
     */
    @Scheduled(fixedDelay = 20 * 60 * 1000, initialDelay = 25 * 1000)
    public void refreshOnlineCheckTask() {

        onlineCheckTask.execute();
    }
}
