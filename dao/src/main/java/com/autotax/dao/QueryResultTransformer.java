package com.autotax.dao;

public interface QueryResultTransformer<E, T> {

    T transaform(E e);
}
