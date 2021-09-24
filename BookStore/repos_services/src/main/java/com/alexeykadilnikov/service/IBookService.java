package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;

import java.util.Comparator;
import java.util.List;

public interface IBookService {
    BookDto addBook(long id, int count);
    List<BookDto> getAll();
    List<Book> sort(List<Book> books, Comparator<Book> comparator);
    BookDto getById(long id);
    void createRequest(Request request, long id);
    void createBook(Book book);
    String getDescription(Long id);
    List<Book> getOldBooks(int monthsAmount);
    BookDto writeOff(long bookId);
    List<BookDto> getStaleBooks(String sortBy, String direction);
    List<BookDto> sortBy(String sortBy, String direction);
}
