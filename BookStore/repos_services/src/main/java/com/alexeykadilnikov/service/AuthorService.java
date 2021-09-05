package com.alexeykadilnikov.service;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.dao.AuthorDAO;
import com.alexeykadilnikov.entity.Author;

import java.util.List;

@Singleton
public class AuthorService implements IAuthorService {

    @InjectBean
    private AuthorDAO authorDAO;

    @Override
    public List<Author> findAll() {
        return authorDAO.findAll();
    }

    @Override
    public Author getById(Long id) {
        return authorDAO.getById(id);
    }

    @Override
    public void save(Author author) {
        authorDAO.save(author);
    }

    @Override
    public void delete(Author author) {
        authorDAO.delete(author);
    }

    @Override
    public void saveAll(List<Author> all) {
        for(Author author : all) {
            authorDAO.save(author);
        }
    }
}
