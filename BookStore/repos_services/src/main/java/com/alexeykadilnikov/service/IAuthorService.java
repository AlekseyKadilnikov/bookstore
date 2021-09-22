package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Author;

import java.util.List;

public interface IAuthorService {
    List<Author> findAll();
    Author getById(Long id);
    void save(Author entity);
    void delete(Author entity);
}
