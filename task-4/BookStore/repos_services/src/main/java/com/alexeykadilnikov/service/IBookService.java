package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;

public interface IBookService {
    void setBookStatus(int index, boolean status);
    String showBook(int index);
    Book[] getAll();
}
