package com.purcell.repository;


import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ICacheRepository<K, V> {
    void put(K key, V value);

    void multiPut(Map<K,V> keyValues);

    V get(K key);

    List<V> multiGet(Collection<K> keys);

    void delete(K key);
}
