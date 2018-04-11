package com.tiza.gw.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Description: DbpDecoder
 * Author: DIYILIU
 * Update: 2018-04-11 11:29
 */
public class DbpDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 2) {
            return;
        }

        in.markReaderIndex();
        int len = in.readUnsignedShort();
        if (in.readableBytes() < (len - 2)) {

            in.resetReaderIndex();
            return;
        }

        in.resetReaderIndex();
        byte[] messages = new byte[len];
        in.readBytes(messages);

        out.add(Unpooled.copiedBuffer(messages));
    }
}
