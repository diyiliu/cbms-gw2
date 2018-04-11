package com.tiza.gw.support.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-28 09:39)
 * Version: 1.0
 * Updated:
 */

@Getter
@Setter
@Component
public class LoadConfigBean {

    @Value("${my.outAddress}")
    private String outAddress = "";
    @Value("${my.inAddress}")
    private String inAddress = "";

    @Value("${my.outPort}")
    private String outPort = "";
    @Value("${my.inPort}")
    private String inPort = "";

    @Value("${my.maxOnline}")
    private Integer maxVtNum = 3000;

    private Integer onLineVtNum = 0;
}
