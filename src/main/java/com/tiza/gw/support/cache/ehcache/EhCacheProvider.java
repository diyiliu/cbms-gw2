package com.tiza.gw.support.cache.ehcache;


import com.tiza.gw.support.cache.CacheException;
import com.tiza.gw.support.cache.ICache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-6-21
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class EhCacheProvider implements ICache {
    private Ehcache cache;

    EhCacheProvider(CacheManager cacheManager, String cacheName) {
        cache = cacheManager.getCache(cacheName);
    }

    @Override
    public void clear() throws CacheException {
        cache.removeAll();
    }

    @Override
    public void put(String key, Object value) throws CacheException {
        Element element = new Element(key, value);
        cache.put(element);
    }

    @Override
    public void put(Map<?, ?> map) throws CacheException {
        List<Element> elements = new ArrayList<Element>();
        Set<?> keySet = map.keySet();
        Iterator<?> keyIterator = keySet.iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next().toString();
            elements.add(new Element(key, map.get(key)));
        }
        cache.putAll(elements);
    }

    @Override
    public boolean containsKey(String key) throws CacheException {
        return cache.isKeyInCache(key);
    }

    @Override
    public Object get(String key) throws CacheException {
        Element element = cache.get(key);
        if (element != null) {
            return element.getObjectValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Map<String, Object> get(Collection<String> keys) throws CacheException {
        Map<Object, Element> elementMap = cache.getAllWithLoader(keys, null);
        Map<String, Object> rtnItems = new HashMap<String, Object>();
        Iterator<Object> keyIterator = elementMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            Element element = elementMap.get(keyIterator.next());
            rtnItems.put(element.getObjectKey().toString(), element.getObjectValue());
        }
        return rtnItems;
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<Object> getAll(Collection<String> keys) throws CacheException {
        if (keys == null)
            return null;
        List<Object> dataList = new ArrayList<Object>();
        Map<Object, Element> elementMap = cache.getAllWithLoader(keys, null);
        Iterator<Object> keyIterator = elementMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            Element element = elementMap.get(keyIterator.next());
            dataList.add(element.getObjectValue());
        }
        return dataList;
    }

    @Override
    public boolean remove(String key) throws CacheException {
        return cache.remove(key);
    }

    @Override
    public int size() throws CacheException {
        return cache.getSize();
    }

    @SuppressWarnings("unchecked")
	@Override
    public Set<Object> getKeys() throws CacheException {
        return new HashSet<Object>(cache.getKeys());
    }
}
