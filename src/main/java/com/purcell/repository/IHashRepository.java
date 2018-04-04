package com.purcell.repository;

import java.util.Collection;
import java.util.List;


public interface IHashRepository<V> {
    void put(V obj);

    void multiPut(Collection<V> keys);

    V get(V key);

    List<V> multiGet(Collection<V> keys);

    void delete(V key);

    List<V> getObjects();

    void delete();
}