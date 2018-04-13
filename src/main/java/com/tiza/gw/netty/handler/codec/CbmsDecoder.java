package com.tiza.gw.netty.handler.codec;

import com.tiza.gw.support.utils.CommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Description: CbmsDecoder
 * Author: DIYILIU
 * Update: 2018-04-10 13:54
 */

@Slf4j
public class CbmsDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        String host = ctx.channel().remoteAddress().toString().trim().replaceFirst("/", "");

        // 主协议头长度
        if (in.readableBytes() < 12) {

            return;
        }

        in.markReaderIndex();
        int len = in.readUnsignedShort();

        // 过滤错误数据
        int protocol = in.readByte();
        if (protocol != 1){
            log.error("[{}]错误数据, 长度[{}], 协议版本号[{}], 断开连接!", host, len, protocol);
            ctx.close();
            return;
        }

        if (in.readableBytes() < (len - 3)) {

            in.resetReaderIndex();
            return;
        }

        in.resetReaderIndex();
        byte[] messages = new byte[len];
        in.readBytes(messages);

        log.debug("收到[{}]数据;[{}]", host, CommonUtils.byteToString(messages));
        out.add(Unpooled.copiedBuffer(messages));
    }
}
