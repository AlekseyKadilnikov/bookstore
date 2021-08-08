package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.BookComparator;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.RequestService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class RequestController {
    private static RequestController instance;

    private final RequestService requestService;

    private RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    public static RequestController getInstance() {
        if(instance == null) {
            instance = new RequestController(RequestService.getInstance());
        }
        return instance;
    }

    public void search(String request) {
        Set<Book> foundBooks = requestService.createRequest(request);
        List<Book> books = new ArrayList<>(foundBooks);
        System.out.println(books.toString());
    }

    public void sort(int bookId, Comparator<Request> comparator) {
        BookService bookService = BookService.getInstance();
        Book book = bookService.getByIndex(bookId);
        List<Request> requests = requestService.sort(book, comparator);
        System.out.println(requests.toString());
    }
}
