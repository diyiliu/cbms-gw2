package com.tiza.gw.protocol.cmd;

import com.tiza.gw.support.bean.GpsInfoBean;
import com.tiza.gw.protocol.CbBaseParser;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-09 10:50)
 * Version: 1.0
 * Updated:
 */
@Component
public class Cmd_02 extends CbBaseParser {
    private final int cmdId = 0x02;

    @Override
    public void parser(Object message, Object ctx) {
        super.responseGpsClient((ChannelHandlerContext) ctx, (GpsInfoBean) message, new byte[]{
                0x12, 0x01, 0x00, 0x00
        });
    }

    @Override
    public Integer getCmdId() {
        return this.cmdId;
    }
}
