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

    public Set<Book> createRequest(String name, int count) {
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

        Set<Long> bookIdSet = new HashSet<>();
        if(booksByAuthor.isEmpty() && !booksByName.isEmpty()) {
            for(Book book : booksByName) {
                bookIdSet.add(book.getId());
            }
            Request request = new Request(name, bookIdSet);
            for(Book book : booksByName) {
                bookRepository.addRequest(request, count, book.getId());
            }
            return booksByName;
        }
        else if(!booksByAuthor.isEmpty() && booksByName.isEmpty()){
            for(Book book : booksByAuthor) {
                bookIdSet.add(book.getId());
            }
            Request request = new Request(name, bookIdSet);
            for(Book book : booksByAuthor) {
                bookRepository.addRequest(request, count, book.getId());
            }
            return booksByAuthor;
        }
        else {
            booksByAuthor.retainAll(booksByName);
            for(Book book : booksByAuthor) {
                bookIdSet.add(book.getId());
            }
            Request request = new Request(name, bookIdSet);
            for(Book book : booksByAuthor) {
                bookRepository.addRequest(request, count, book.getId());
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

    public List<Request> sort(Book book, Comparator<Request> comparator) {
        List<Request> requests = book.getCommonRequests();
        requests.sort(comparator);
        return requests;
    }
}
