package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final IBookService bookService;

    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    public void sortByName(int mode) {
        bookService.sortByName(mode);
    }

    public void sortByPrice(int mode) {
        bookService.sortByPrice(mode);
    }

    public void sortByPublicationYear(int mode) {
        bookService.sortByPublicationYear(mode);
    }

    public void sortByCount(int mode) {
        bookService.sortByCount(mode);
    }

    public void getStaleBooksByDate(int mode) {
        bookService.getStaleBooksByDate(mode);
    }

    public void getStaleBooksByPrice(int mode) {
        bookService.getStaleBooksByPrice(mode);
    }

    public void writeOff(long bookId) {
        bookService.writeOff(bookId);
    }

    public List<Book> getAll() {
        return bookService.getAll();
    }

    public void saveAll(List<Book> bookList) {
        bookService.saveAll(bookList);
    }

    public void showDescription(Long bookId) {
        System.out.println(bookService.getDescription(bookId));
    }

    public void addBook(long bookId, int count){
        bookService.addBook(bookId, count);
    }

    public void importBooks(String path) {}

    public void exportBooks(String path, String bookIds) {}
}
