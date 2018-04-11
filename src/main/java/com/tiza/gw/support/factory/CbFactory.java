package com.tiza.gw.support.factory;

import com.tiza.gw.protocol.CbBaseParser;
import com.tiza.gw.support.utils.SpringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-02 15:31)
 * Version: 1.0
 * Updated:
 */

@Component
public class CbFactory extends AbstractProFactory<Integer , CbBaseParser> {
    private static final Map<Integer, CbBaseParser> SONS = new ConcurrentHashMap();
    @Override
    public void addCmd(Integer key, CbBaseParser val) {
        SONS.put(key , val);
    }

    @Override
    public CbBaseParser getCmd(Integer key) {
        return SONS.get(key);
    }

    @Override
    public void init() {
        Map<String, CbBaseParser> parsers = SpringUtil.getBeansOfType(CbBaseParser.class);
        Collection<CbBaseParser> vals = parsers.values();
        if (!CollectionUtils.isEmpty(vals)) {
            for (CbBaseParser cmdParse : vals) {
                if(null != cmdParse.getCmdId()){
                    this.addCmd(cmdParse.getCmdId(), cmdParse);
                }
            }
        }
    }

    @Override
    public int getProtocolId() {
        return 0;
    }
}
