package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Author;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class AuthorRepository implements IAuthorRepository {

    List<Author> authors = new ArrayList<>();

    @Override
    public List<Author> findAll() {
        return authors;
    }

    @Override
    public Author getById(Long aLong) {
        return authors.stream()
                .filter(author -> author.getId() == aLong)
                .findAny()
                .orElse(null);
    }

    @Override
    public void save(Author author) {
        authors.add(author);
    }

    @Override
    public void delete(Author author) {
        authors.remove(author);
    }

    @Override
    public void saveAll(List<Author> all) {
        authors.addAll(all);
    }
}
