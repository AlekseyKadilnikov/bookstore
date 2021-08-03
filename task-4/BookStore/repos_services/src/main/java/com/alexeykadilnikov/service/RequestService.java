package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderComparator;
import com.alexeykadilnikov.RequestComparator;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.BookRepository;

import java.util.*;

public class RequestService implements IRequestService {
    private final BookRepository bookRepository;

    public RequestService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void createRequest(String name) {
        String[] words = name.split(" ");
        Book[] books = bookRepository.findAll();
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
        }
        else if(!booksByAuthor.isEmpty() && booksByName.isEmpty()){
            for(Book book : booksByAuthor) {
                book.addRequest(new Request(name, RequestStatus.COMMON));
            }
        }
        else {
            booksByAuthor.retainAll(booksByName);
            for(Book book : booksByAuthor) {
                book.addRequest(new Request(name, RequestStatus.COMMON));
            }
        }
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
