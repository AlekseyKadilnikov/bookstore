package repository;

import java.util.List;

public interface IRepository<T,ID> {
    List<T> findAll();
    T getById(ID id);
    void save(T entity);
    void delete(T entity);
}