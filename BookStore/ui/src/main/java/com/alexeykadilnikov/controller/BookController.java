package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.service.BookService;

import java.util.Comparator;
import java.util.List;

public class BookController {
    private static BookController instance;

    private final BookService bookService;

    private BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public static BookController getInstance() {
        if(instance == null) {
            instance = new BookController(BookService.getInstance());
        }
        return instance;
    }

    public void sort(List<Book> sortedBooks, Comparator<Book> comparator) {
        sortedBooks = bookService.sort(sortedBooks, comparator);
        System.out.println(sortedBooks.toString());
    }

    public List<Book> getAll() {
        return bookService.getAll();
    }

    public List<Book> getStaleBooks(int months) {
        return bookService.getOldBooks(months);
    }

    public void showDescription(int bookId) {
        Book book = bookService.getByIndex(bookId);
        System.out.println(book.getDescription());
    }
}
