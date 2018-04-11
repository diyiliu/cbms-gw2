package com.tiza.gw.support.config;

import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.cache.ram.RamCacheProvider;
import com.tiza.gw.support.client.MonitorClient;
import com.tiza.gw.support.other.SqlXmlCache;
import com.tiza.gw.support.utils.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Description: SpringConfig
 * Author: DIYILIU
 * Update: 2018-04-04 09:36
 */

@Configuration
public class SpringConfig {

    /**
     * spring 工具类
     *
     * @return
     */
    @Bean
    public SpringUtil springUtil() {

        return new SpringUtil();
    }

    /**
     * spring jdbcTemplate
     *
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){

        return new JdbcTemplate(dataSource);
    }

    /**
     * 初始化SQL
     *
     * @return
     */
    @Bean
    public SqlXmlCache sqlXmlCache(){
        SqlXmlCache xmlCache = new SqlXmlCache();
        xmlCache.Init();

        return xmlCache;
    }

    /**
     * 命令监控
     *
     * @return
     */
    @Bean
    public MonitorClient monitorClient(){
        MonitorClient monitorClient = new MonitorClient();
        monitorClient.init();

        return monitorClient;
    }

    @Bean
    public ICache monitorCacheProvider() {

        return new RamCacheProvider();
    }


    /**
     * 在线设备缓存
     *
     * @return
     */
    @Bean
    public ICache onlineCacheProvider() {

        return new RamCacheProvider();
    }

    /**
     * 车辆信息
     *
     * @return
     */
    @Bean
    public ICache vehicleInfoCacheProvider() {

        return new RamCacheProvider();
    }

    /**
     * 工况信息
     *
     * @return
     */
    @Bean
    public ICache canInfoCacheProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache canStatusProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache canPackageProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache paramsCacheProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache alarmInfoCacheProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache fenceCacheProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache fenceAlarmInfoCacheProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache workAlarmInfoCacheProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache inOutAlarmInfoCacheProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache alertItemCacheProvider() {

        return new RamCacheProvider();
    }

    @Bean
    public ICache nameKeyCacheProvider() {

        return new RamCacheProvider();
    }
}
