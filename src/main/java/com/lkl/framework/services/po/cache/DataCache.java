package com.lkl.framework.services.po.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lkl.framework.services.po.meta.POMetadata;

/**
 * sql缓存
 * 
 * @author liaokailin
 * @version $Id: DataCache.java, v 0.1 2015年10月17日 下午11:58:08 liaokailin Exp $
 */
public class DataCache {
    /**
     * 并发操作hashmap容易操作闭环死锁
     */
    public static final Map<String, String>       INSERT_SQL_HOLDER = new CacheConcurrentHashMap<String, String>(64);
    public static final Map<String, String>       UPDATE_SQL_HOLDER = new CacheConcurrentHashMap<String, String>(64);
    public static final Map<String, String>       DELETE_SQL_HOLDER = new CacheConcurrentHashMap<String, String>(64);
    public static final Map<String, String>       SELECT_SQL_HOLDER = new CacheConcurrentHashMap<String, String>(64);
    public static final Map<Class<?>, POMetadata> META_HOLDER       = new CacheConcurrentHashMap<Class<?>, POMetadata>(
                                                                        64);
    public static final Integer                   MAX_SQL_SIZE      = 128;

    static class CacheConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {
        private static final long serialVersionUID = -8407223549946651210L;

        public CacheConcurrentHashMap(int i) {
            super(i);
        }

        public V put(K key, V value) {
            if (this.size() > MAX_SQL_SIZE) {
                this.clear();
            }
            return super.put(key, value);
        }
    }
}
