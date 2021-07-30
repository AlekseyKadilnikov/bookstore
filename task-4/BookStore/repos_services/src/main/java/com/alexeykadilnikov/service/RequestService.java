package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.RequestRepository;

public class RequestService implements IRequestService {
    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public void createRequest(Book book, User user) {
        if(book.isAvailable()) {
            System.out.println("Request couldn't be created: book is available");
            return;
        }
        Request request = new Request(book, user);
        requestRepository.save(request);
        System.out.println("Request id = " + request.getId() + " created");
    }

    @Override
    public String showRequest() {
        //return requestRepository.getRequest().toString();
        return null;
    }

    @Override
    public void cancelRequest() {
        // System.out.println("Request id = " + requestRepository.getRequest().getId() + " closed");
    }
}
