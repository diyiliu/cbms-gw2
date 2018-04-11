package com.tiza.gw.support.cache.ram;



import com.tiza.gw.support.cache.CacheException;
import com.tiza.gw.support.cache.ICache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-17 09:50)
 * Version: 1.0
 * Updated:
 */
public class RamCacheProvider implements ICache {
    Map<Object, Object> cacheMap = new ConcurrentHashMap<Object, Object>(256);

    public void clear() throws CacheException {
        cacheMap.clear();
    }

    @Override
    public void put(String key, Object value) throws CacheException {
        cacheMap.put(key, value);
    }

    @Override
    public void put(Map<?, ?> map) throws CacheException {
        cacheMap.putAll(map);
    }

    @Override
    public boolean containsKey(String key) throws CacheException {
        return cacheMap.containsKey(key);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object get(String key) throws CacheException {
        return containsKey(key) ? this.cacheMap.get(key) : null;
    }

    @Override
    public Map<String, Object> get(Collection<String> keys) throws CacheException {
        if (keys == null || keys.isEmpty())
            return null;
        Map<String, Object> rtnItems = new HashMap<String, Object>();
        Iterator<String> keyIterator = keys.iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next().toString();
            Object value = this.get(key);
            if (value != null) {
                rtnItems.put(key, value);
            }
        }
        return rtnItems;
    }

    @Override
    public List<Object> getAll(Collection<String> keys) throws CacheException {
        if (keys == null || keys.isEmpty())
            return null;
        List<Object> values = new ArrayList<Object>();
        Iterator<String> keyIterator = keys.iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next().toString();
            Object value = this.get(key);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }

    @Override
    public boolean remove(String key) throws CacheException {
        this.cacheMap.remove(key);
        return true;
    }

    @Override
    public int size() throws CacheException {
        return this.cacheMap.size();
    }

    @Override
    public Set<Object> getKeys() throws CacheException {
        return this.cacheMap.keySet();
    }
}
