package com.tiza.gw.support.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-17 09:50)
 * Version: 1.0
 * Updated:
 */
public interface ICache {
	/**
	 * Empty the cache data
	 * @throws CacheException
	 */
    void clear() throws CacheException;

    /**
     * Add an object to the cache
     * @param key
     * @param value
     * @throws CacheException
     */
    void put(String key, Object value) throws CacheException;
    
    /**
     * 
     * @param map
     * @throws CacheException
     */
    void put(Map<?, ?> map) throws CacheException;

    /**
     * 
     * @param key
     * @return
     * @throws CacheException
     */
    boolean containsKey(String key) throws CacheException;

    Object get(String key) throws CacheException;

    Map<String, Object> get(Collection<String> keys) throws CacheException;

    List<?> getAll(Collection<String> keys) throws CacheException;

    boolean remove(String key) throws CacheException;

    int size() throws CacheException;

    Set<Object> getKeys() throws CacheException;
}
