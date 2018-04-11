package com.tiza.gw.netty.client.base;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: ClientThread
 * Author: DIYILIU
 * Update: 2018-04-10 15:26
 */

public class ClientThread extends Thread {

    private String host;

    private Integer port;

    protected ChannelHandler channelHandler;

    protected ChannelFuture future;

    // 重连次数
    protected int reconnect = 0;

    public void init() {

        this.start();
    }

    @Override
    public void run() {
        connectServer(host, port);
    }

    public void connectServer(String host, int port){


    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
