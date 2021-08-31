package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;

public interface IBookRepository extends IRepository<Book, Long> {
    void addRequest(Request request, int count, Book book);
}
