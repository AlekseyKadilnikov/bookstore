package com.alexeykadilnikov.dao;

import java.util.List;

public interface IGenericDAO<T, PK> {
    List<T> findAll();
    List<T> findAll(String hql);
    T getById(PK id);
    T save(T entity);
    void delete(T entity);
    void update(T entity);
}
