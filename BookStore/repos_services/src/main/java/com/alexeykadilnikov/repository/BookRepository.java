package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class BookRepository implements IBookRepository {

    private List<Book> books = new ArrayList<>();

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Book getById(Long id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findAny()
                .orElse(null);
    }

    @Override
    public void save(Book book) {
        books.add(book);
    }

    @Override
    public void delete(Book book) {
        books.remove(book);
    }

    @Override
    public void saveAll(List<Book> all) {
        books = all;
    }

    public void addRequest(Request request, int count, long id) {
        Book book = getById(id);
        List<Request> commonRequests = book.getCommonRequests();
        Request[] orderRequests = book.getOrderRequests();
        if(request.getStatus() == RequestStatus.COMMON) {
            for(Request r : commonRequests) {
                if(request.getName().equals(r.getName())) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
            commonRequests.add(request);
        }
        else {
            for(Request r : orderRequests) {
                if(request.getName().equals(r.getName()) && request.getStatus() == r.getStatus()) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
        }
//        RequestRepository requestRepository = RequestRepository.getInstance();
//        requestRepository.save(request);
    }
}
