package com.tiza.gw.protocol.cmd;

import com.tiza.gw.protocol.CbBaseParser;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-09 10:50)
 * Version: 1.0
 * Updated:
 */

@Component
public class Cmd_00 extends CbBaseParser {
    private final int cmdId = 0x00;

    @Override
    public void parser(Object message , Object ctx) {

    }

    @Override
    public Integer getCmdId() {
        return this.cmdId;
    }
}