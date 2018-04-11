package com.tiza.gw.support.client;

import com.tiza.gw.support.cache.ICache;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Scanner;

/**
 * Description: MonitorClient
 * Author: DIYILIU
 * Update: 2016-01-25 11:10
 */
public class MonitorClient implements IClient, Runnable {

    protected Logger logger = LoggerFactory.getLogger(MonitorClient.class);

    @Resource
    private ICache monitorCacheProvider;

    @Resource(name = "vehicleInfoCacheProvider")
    private ICache vehicleCacheProvider;

    private MonitorClient monitorClient;

    private Options options;
    private CommandLineParser parser;
    private CommandLine cmd;

    @Override
    public void init() {
        monitorClient = this;

        options = new Options();
        options.addOption("h", "help", false, "help information");
        options.addOption("m", "monitor", true, "监控车辆 格式[-m vin '开始监控'; -m c '取消监控']");
        parser = new DefaultParser();
        start();
    }

    @Override
    public void start() {
        new Thread(monitorClient, "MONITOR_CLIENT").start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        for (; ; ) {
            String in = scanner.nextLine();

            try {
                cmd = parser.parse(options, new String[]{in});

                if (cmd.hasOption("h")) {

                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("help", options);
                } else if (cmd.hasOption("m")) {
                    String vinCode = cmd.getOptionValue("m").trim();

                    if (vinCode.trim().equalsIgnoreCase("C")){
                        System.out.println("取消监控车辆VIN:" +  monitorCacheProvider.getKeys());
                        monitorCacheProvider.clear();
                    }else if (vehicleCacheProvider.containsKey(vinCode)){
                        System.out.println("开始监控车辆VIN[" + vinCode + "]");
                        monitorCacheProvider.put(vinCode, new Date());
                    }else {
                        System.out.println("不存在车辆VIN[" + vinCode + "]");
                    }
                } else {
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("cmd error", options);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("监控车辆异常！");
            }
        }
    }
}
