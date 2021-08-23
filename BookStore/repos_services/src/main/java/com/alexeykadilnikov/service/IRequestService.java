package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public interface IRequestService {
    Set<Book> createRequest(String name, int count);

    List<Request> sort(Book book, Comparator<Request> comparator);

    List<Request> getAll();

    void saveAll(List<Request> requestList);

    Request getById(long id);
}

