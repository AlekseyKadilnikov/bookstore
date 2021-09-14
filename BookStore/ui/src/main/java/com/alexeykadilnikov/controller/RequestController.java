package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.service.IBookService;
import com.alexeykadilnikov.service.IRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Singleton
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @InjectBean
    private IRequestService requestService;
    @InjectBean
    private IBookService bookService;


    public void search(String request) {
        Set<Book> foundBooks = requestService.createRequest(request, 1);
        System.out.println(foundBooks);
    }

    public void getRequestsForBookSortedByCount(long bookId, int mode) {
        requestService.getRequestsForBookSortedByCount(bookId, mode);
    }

    public void getRequestsForBookSortedByName(long bookId, int mode) {
        requestService.getRequestsForBookSortedByName(bookId, mode);
    }

    public List<Request> getAll() {
        return requestService.getAll();
    }

    public void saveAll(List<Request> requestList) {
        requestService.saveAll(requestList);
    }

    public void importRequests(String path) {}

    public void exportRequests(String path, String bookIds) {}
}
