package com.alexeykadilnikov.dao;

import java.util.List;

public interface IGenericDao<T, PK> {
    List<T> findAll();
    T getById(PK id);
    void save(T entity);
    void delete(PK id);
    void update(T entity);
}
