package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderComparator;
import com.alexeykadilnikov.RequestComparator;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.RequestRepository;

import java.util.Arrays;

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
        for(Request request : requestRepository.findAll()) {
            if(request.getBook() == book && request.getUser() == user) {
                request.addAmount();
                System.out.println("Request id = " + request.getId() + ", amount added");
                return;
            }
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

    @Override
    public Request[] getAll() {
        return requestRepository.findAll();
    }

    public Request[] sortByAmountAscending(Book book, Request[] requests) {
        requests = getRequestsForOneBook(book, requests);
        Arrays.sort(requests, RequestComparator.AmountComparatorAscending);
        return requests;
    }

    public Request[] sortByAmountDescending(Book book, Request[] requests) {
        requests = getRequestsForOneBook(book, requests);
        Arrays.sort(requests, RequestComparator.AmountComparatorDescending);
        return requests;
    }

    private Request[] getRequestsForOneBook(Book book, Request[] requests) {
        Request[] requestsWithOneBook = new Request[0];
        for (Request request : requests) {
            if(request.getBook() == book) {
                Request[] newRequests = new Request[requestsWithOneBook.length + 1];
                System.arraycopy(requestsWithOneBook, 0, newRequests, 0, requestsWithOneBook.length);
                newRequests[requestsWithOneBook.length] = request;
                requestsWithOneBook = newRequests;
            }
        }
        requests = requestsWithOneBook;
        return requests;
    }
}
