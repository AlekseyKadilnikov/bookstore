package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.dao.IAuthorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService implements IAuthorService {

    private final IAuthorDAO authorDAO;

    @Autowired
    public AuthorService(IAuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

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
