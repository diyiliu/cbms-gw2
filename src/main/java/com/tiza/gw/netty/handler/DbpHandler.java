package com.tiza.gw.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: TlbsHandler
 * Author: DIYILIU
 * Update: 2018-04-10 14:20
 */

@Slf4j
public class DbpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        // 心跳处理
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE == event.state()) {
                //log.warn("读超时...");
            } else if (IdleState.WRITER_IDLE == event.state()) {
                log.info("向DBP发送心跳...");
                ctx.writeAndFlush(Unpooled.copiedBuffer(new byte[]{0x00, 0x03, 0x00}));
            } else if (IdleState.ALL_IDLE == event.state()) {
                //log.warn("读/写超时...");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("服务器异常[{}]!", cause.getMessage());
        cause.printStackTrace();
    }
}
