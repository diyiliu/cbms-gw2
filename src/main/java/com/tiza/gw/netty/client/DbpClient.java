package com.tiza.gw.netty.client;

import com.tiza.gw.netty.client.base.ClientThread;
import com.tiza.gw.netty.handler.DbpHandler;
import com.tiza.gw.netty.handler.codec.DbpDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: DbpClient
 * Author: DIYILIU
 * Update: 2018-04-10 14:16
 */

@Slf4j
public class DbpClient extends ClientThread {
    private static Queue<String> sqlPool = new ConcurrentLinkedQueue();
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    @Override
    public void init() {
        this.start();

        // 发送sql到DBP
        executor.execute(new Runnable() {
            @Override
            public void run() {
                    for (; ; ) {
                        while (!sqlPool.isEmpty()) {
                            if (future != null && future.channel().isActive()) {
                                String sql = sqlPool.poll();
                                byte[] bytes = sql.getBytes();

                                int length = 3 + bytes.length;
                                ByteBuf buf = Unpooled.buffer(length);
                                buf.writeShort(length);
                                buf.writeByte((byte) 1);
                                buf.writeBytes(bytes);

                                future.channel().writeAndFlush(buf);
                            }
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });
    }

    public void connectServer(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        channelHandler = new DbpHandler();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new IdleStateHandler(30, 30, 30))
                                .addLast(new DbpDecoder())
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

            log.info("CBMS客户端, 尝试第{}次重连DBP...", ++reconnect);
            connectServer(host, port);
        }
    }

    public static void sendSQL(String sql) {
        sqlPool.add(sql);
    }
}
