package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Author;
import java.util.List;

public interface IAuthorRepository extends IRepository <Author, Long> {
    @Override
    List<Author> findAll();

    @Override
    Author getById(Long aLong);

    @Override
    void save(Author entity);

    @Override
    void delete(Author entity);

    @Override
    void saveAll(List<Author> all);
}
