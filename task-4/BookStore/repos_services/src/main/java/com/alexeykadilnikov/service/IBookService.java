package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;

public interface IBookService {
    void addBook(int index, int count);
    String showBook(int index);
    Book[] getAll();
}
