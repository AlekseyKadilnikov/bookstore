package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.RequestDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public interface IRequestService {
    Set<Book> createRequest(String name, int count);

    List<Request> sort(Book book, Comparator<Request> comparator);

    List<RequestDto> getAll();

    RequestDto save(RequestDto requestDto);

    RequestDto getById(long id);

    List<Request> sendSqlQuery(String hql);

    List<RequestDto> getRequestsForBook(long bookId, String sortBy, int mode);
}

