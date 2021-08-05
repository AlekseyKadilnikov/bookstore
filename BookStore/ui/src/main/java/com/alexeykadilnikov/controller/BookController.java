package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.utils.Utils;

import java.util.Comparator;
import java.util.List;

public class BookController {
    private static BookController instance;

    private BookService bookService;

    private BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public static BookController getInstance() {
        if(instance == null) {
            instance = new BookController(BookService.getInstance());
        }
        return instance;
    }

    public void sort(Comparator<Book> comparator) {
        List<Book> sortedBooks = bookService.getAll();
        sortedBooks = bookService.sort(sortedBooks, comparator);
        Utils.showBookArray(sortedBooks);
    }
}
