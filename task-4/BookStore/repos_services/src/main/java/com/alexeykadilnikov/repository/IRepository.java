package com.alexeykadilnikov.repository;

public interface IRepository<T,ID> {
    T[] findAll();
    T getById(ID id);
    void save(T entity);
    void delete(T entity);
}
