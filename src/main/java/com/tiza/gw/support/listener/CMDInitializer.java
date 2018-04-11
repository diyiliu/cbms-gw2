package com.tiza.gw.support.listener;

import com.tiza.gw.support.factory.AbstractProFactory;
import com.tiza.gw.support.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * Description: CMDInitializer
 * Author: DIYILIU
 * Update: 2018-04-11 10:13
 */
@Slf4j
public class CMDInitializer implements ApplicationListener {

    private List<Class> protocols;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("协议解析初始化...");
        for (Class protocol : protocols) {
            AbstractProFactory factory = SpringUtil.getBean(protocol);
            factory.init();
        }
    }

    public void setProtocols(List<Class> protocols) {
        this.protocols = protocols;
    }
}