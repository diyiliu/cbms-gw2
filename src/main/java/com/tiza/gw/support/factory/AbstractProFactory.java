package com.tiza.gw.support.factory;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-02 15:29)
 * Version: 1.0
 * Updated:
 */
public abstract class AbstractProFactory<K, V> {

    public abstract void addCmd(K key, V val);

    public abstract V getCmd(K key);

    public abstract void init();

    public abstract int getProtocolId();

}
