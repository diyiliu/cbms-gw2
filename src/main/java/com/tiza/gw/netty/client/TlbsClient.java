package com.tiza.gw.netty.client;

import com.tiza.gw.netty.client.base.ClientThread;
import com.tiza.gw.netty.handler.TlbsHandler;
import com.tiza.gw.netty.handler.codec.CbmsDecoder;
import com.tiza.gw.support.bean.LoadConfigBean;
import com.tiza.gw.support.cache.ICache;
import com.tiza.gw.support.utils.CommonUtils;
import com.tiza.gw.support.utils.JacksonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description: TlbsClient
 * Author: DIYILIU
 * Update: 2018-04-10 14:16
 */

@Slf4j
public class TlbsClient extends ClientThread {
    @Resource
    private LoadConfigBean loadConfigBean;

    @Resource
    private ICache onlineCacheProvider;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Override
    public void init() {
        this.start();

        // 定时发送负载报告
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (future != null && future.channel().isActive()) {
                    sendReport();
                }
            }
        }, 30, 5 * 60, TimeUnit.SECONDS);
    }

    @Override
    public void connectServer(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        channelHandler = new TlbsHandler();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new IdleStateHandler(30, 30, 30))
                                .addLast(new CbmsDecoder())
                                .addLast(channelHandler);
                    }
                });
        try {
            future = bootstrap.connect(host, port).sync();
            reconnect = 0;
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("CBMS客户端, 尝试第{}次重连TLBS...", ++reconnect);
            connectServer(host, port);
        }
    }

    /**
     * 发送负载报告
     */
    private void sendReport() {
        try {
            loadConfigBean.setOnLineVtNum(onlineCacheProvider.size());
            String onlineStr = JacksonUtil.toJson(loadConfigBean);

            byte[] onlineBytes = onlineStr.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(5 + onlineBytes.length);
            buffer.put((byte) 0x02);
            buffer.putInt(onlineBytes.length);
            buffer.put(onlineBytes);
            ByteBuf bf = CommonUtils.parkTlbs(buffer.array(), 0x01);
            future.channel().writeAndFlush(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
