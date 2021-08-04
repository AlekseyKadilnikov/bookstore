package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;

import java.util.List;

public interface IBookService {
    void addBook(int index, int count);
    String showBook(int index);
    List<Book> getAll();
}
