package com.tiza.gw.protocol;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-02 15:32)
 * Version: 1.0
 * Updated:
 */
public abstract class AbstractBaseParser<T> {
    public abstract void parser(Object message , Object ctx);

    public abstract T getCmdId();

}
