package com.purcell.repository;

import java.util.Set;

public interface IRedisSetDemo<K,V> {

    void add(K key, V value);

    boolean isMemberOf(K key, V value);

    Set<V> members(K key);

    V pop(K key);

    void delete(K key);

}