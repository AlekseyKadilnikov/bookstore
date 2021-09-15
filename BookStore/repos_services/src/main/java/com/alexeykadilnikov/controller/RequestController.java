package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.service.IRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    private final IRequestService requestService;

    @Autowired
    public RequestController(IRequestService requestService) {
        this.requestService = requestService;
    }

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
}
