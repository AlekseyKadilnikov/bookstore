package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.service.RequestService;
import com.alexeykadilnikov.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RequestController {
    private static RequestController instance;

    private RequestService requestService;

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
        Utils.showBookArray(books);
    }
}
