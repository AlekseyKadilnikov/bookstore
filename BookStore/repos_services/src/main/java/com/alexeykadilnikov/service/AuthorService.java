package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.mapper.BookMapper;
import com.alexeykadilnikov.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService implements IAuthorService {

    private final IAuthorRepository authorRepository;
    private final BookMapper authorMapper;

    @Autowired
    public AuthorService(IAuthorRepository authorRepository, BookMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author getById(Long id) {
        return authorRepository.getById(id);
    }

    @Override
    public void save(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }
}
