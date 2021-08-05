package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderComparator;
import com.alexeykadilnikov.RequestComparator;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;

import java.util.*;

public class RequestService implements IRequestService {
    private static RequestService instance;

    private final BookRepository bookRepository;

    private RequestService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Set<Book> createRequest(String name) {
        String[] words = name.split(" ");
        List<Book> books = bookRepository.findAll();
        Set<Book> booksByAuthor = new HashSet<>();
        Set<Book> booksByName = new HashSet<>();
        for(String word : words) {
            for(Book book : books) {
                if(book.getName().toLowerCase().contains(word.toLowerCase())) {
                    booksByName.add(book);
                }
                else if(book.getAuthor().toLowerCase().contains(word.toLowerCase())) {
                    booksByAuthor.add(book);
                }
            }
        }

        if(booksByAuthor.isEmpty() && !booksByName.isEmpty()) {
            for(Book book : booksByName) {
                book.addRequest(new Request(name, RequestStatus.COMMON));
            }
            return booksByName;
        }
        else if(!booksByAuthor.isEmpty() && booksByName.isEmpty()){
            for(Book book : booksByAuthor) {
                book.addRequest(new Request(name, RequestStatus.COMMON));
            }
            return booksByAuthor;
        }
        else {
            booksByAuthor.retainAll(booksByName);
            for(Book book : booksByAuthor) {
                book.addRequest(new Request(name, RequestStatus.COMMON));
            }
            return booksByAuthor;
        }
    }

    public static RequestService getInstance() {
        if(instance == null) {
            instance = new RequestService(BookRepository.getInstance());
        }
        return instance;
    }

    public List<Request> sortByAmountAscending(Book book) {
        List<Request> requests = book.getCommonRequests();
        requests.sort(RequestComparator.AmountComparatorAscending);
        return requests;
    }

    public List<Request> sortByAmountDescending(Book book) {
        List<Request> requests = book.getCommonRequests();
        requests.sort(RequestComparator.AmountComparatorDescending);
        return requests;
    }

    public List<Request> sortByNameAscending(Book book) {
        List<Request> requests = book.getCommonRequests();
        requests.sort(RequestComparator.NameComparatorAscending);
        return requests;
    }

    public List<Request> sortByNameDescending(Book book) {
        List<Request> requests = book.getCommonRequests();
        requests.sort(RequestComparator.NameComparatorDescending);
        return requests;
    }
}
