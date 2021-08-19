package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;

import java.util.List;

public interface IBookService {
    void addBook(long id, int count);
    String showBook(long id);
    List<Book> getAll();
}
