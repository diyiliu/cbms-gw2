package com.tiza.gw.support.task.timer;

import com.tiza.gw.support.dao.OnlineCheckDao;
import com.tiza.gw.support.entity.OnlineEntity;
import com.tiza.gw.support.task.ITask;
import com.tiza.gw.support.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: OnlineCheckTask
 * Author: DIYILIU
 * Update: 2018-04-10 16:46
 */

@Slf4j
@Service
public class OnlineCheckTask implements ITask {

    @Resource(name = "onlineCheckDaoImpl")
    private OnlineCheckDao onlineCheckDao;

    @Override
    public void execute() {
        log.info("刷新检测车辆在线状态...");
        List res = onlineCheckDao.selectOnline();
        this.dealResult(res);
    }



    /**
     * 分析终端是否 已经超过 20分钟未更新 设置ACC状态为关
     *
     * @param list
     */
    private void dealResult(List<OnlineEntity> list) {

        for (OnlineEntity oe : list) {
            // acc状态 没关闭 且 最新时间与当前时间大于 20分钟 设置当前状态表中的ACC状态关闭
            long time = Math.abs(System.currentTimeMillis() - oe.getCreateTime().getTime()) / 1000;
            if (!(oe.getAccStatus() == 0) && time > 20 * 60) {

                StringBuilder sb = new StringBuilder();
                sb.append(" update BS_VEHICLEGPSINFO set accstatus= 0 where vehicleid =");
                sb.append(oe.getVehicleId());

                CommonUtils.dealInfoTODb(sb.toString());
            }
        }
    }
}
