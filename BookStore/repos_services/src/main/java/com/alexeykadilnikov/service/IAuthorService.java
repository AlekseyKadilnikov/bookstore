package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.AuthorDto;
import com.alexeykadilnikov.entity.Author;

import java.util.List;

public interface IAuthorService {
    List<AuthorDto> findAll();
    AuthorDto getById(Long id);
    AuthorDto save(AuthorDto entity);
    void delete(AuthorDto entity);
}
