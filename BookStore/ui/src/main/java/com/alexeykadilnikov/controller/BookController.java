package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.service.BookService;

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
}
