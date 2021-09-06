package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;

import java.util.Comparator;
import java.util.List;

public interface IBookService {
    void saveAll(List<Book> bookList);

    void addBook(long id, int count);
    String showBook(long id);
    List<Book> getAll();
    List<Book> sort(List<Book> books, Comparator<Book> comparator);
    Book getById(long id);
    void createRequest(Request request, long id);
    void createBook(Book book);
    String getBookDescription(Book book);
    List<Book> getOldBooks(int monthsAmount);
    void writeOff(long bookId);
    List<Book> sendSqlQuery(String hql);
}
