package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.utils.QueryBuilder;

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
    String getDescription(Long id);
    List<Book> getOldBooks(int monthsAmount);
    void writeOff(long bookId);
    List<Book> sendSqlQuery(String hql);
    void sortByName(int mode);
    void sortByPrice(int mode);
    void sortByPublicationYear(int mode);
    void sortByCount(int mode);
    void getStaleBooksByDate(int mode);
    void getStaleBooksByPrice(int mode);
}
