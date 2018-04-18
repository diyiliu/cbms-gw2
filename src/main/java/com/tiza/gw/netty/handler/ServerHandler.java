package com.tiza.gw.netty.handler;

import com.tiza.gw.protocol.AbstractBaseParser;
import com.tiza.gw.support.bean.GpsInfoBean;
import com.tiza.gw.support.factory.AbstractProFactory;
import com.tiza.gw.support.utils.CommonUtils;
import com.tiza.gw.support.utils.SpringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Description: ServerHandler
 * Author: DIYILIU
 * Update: 2018-04-10 13:34
 */

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final String host = ctx.channel().remoteAddress().toString().trim().replaceFirst("/", "");
        log.info("[{}]建立连接...", host);

        // 断开连接
        ctx.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (future.isDone()) {
                    log.info("[{}]断开连接...", host);
                }
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        AbstractProFactory factory = SpringUtil.getBean("cbFactory");
        try {
            ByteBuf bf = (ByteBuf) msg;

            int infoLen = bf.readUnsignedShort();
            int protocolVersion = bf.readUnsignedByte();
            int comeForm = bf.readUnsignedByte();

            byte[] gpsTimeBytes = new byte[6];
            bf.readBytes(gpsTimeBytes);
            Date gpsDate = CommonUtils.getGpsTime(gpsTimeBytes);

            int infoType = bf.readUnsignedByte();
            int cmdId = bf.readUnsignedByte();

            byte[] messages = null;
            if (bf.readableBytes() > 0) {
                messages = new byte[bf.readableBytes()];
                bf.readBytes(messages);
                return;
            }

            AbstractBaseParser parser = (AbstractBaseParser) factory.getCmd(cmdId);
            if (null == parser) {
                log.error("不存在{}指令ID的解析方法！", Integer.toHexString(cmdId));
                return;
            }

            GpsInfoBean bean = new GpsInfoBean(infoLen, protocolVersion, comeForm, gpsDate, infoType, cmdId, messages);
            parser.parser(bean, ctx);
        } catch (Exception e) {
            log.error("数据解析异常！", e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        String key = ctx.channel().remoteAddress().toString().trim().replaceFirst("/", "");

        // 心跳处理
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE == event.state()) {
                log.info("读超时...[{}], 断开连接！", key);
                ctx.close();
            } else if (IdleState.WRITER_IDLE == event.state()) {
                log.warn("写超时...");

            } else if (IdleState.ALL_IDLE == event.state()) {
                log.warn("读/写超时...");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("服务器异常[{}], 关闭连接!", cause.getMessage());
        cause.printStackTrace();
        // 关闭连接
        ctx.close();
    }
}
