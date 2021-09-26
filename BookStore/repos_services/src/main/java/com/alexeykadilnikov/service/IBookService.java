package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.BookDto;
import java.util.List;

public interface IBookService {
    BookDto addBook(long id, int count);
    List<BookDto> getAll();
    BookDto getById(long id);
    String getDescription(Long id);
    BookDto writeOff(long bookId);
    List<BookDto> getStaleBooks(String sortBy, String direction);
    List<BookDto> sortBy(String sortBy, String direction);
}
