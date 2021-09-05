package com.alexeykadilnikov.dao;

import java.util.List;

public interface IGenericDAO<T, PK> {
    List<T> findAll();
    T getById(PK id);
    void save(T entity);
    void delete(T entity);
    void update(T entity);
}
