package com.alexeykadilnikov.service;

import com.alexeykadilnikov.annotation.InjectBean;
import com.alexeykadilnikov.annotation.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.repository.IBookRepository;
import com.alexeykadilnikov.repository.IRequestRepository;

import java.util.*;

@Singleton
public class RequestService implements IRequestService {
    @InjectBean
    private IBookRepository bookRepository;
    @InjectBean
    private IRequestRepository requestRepository;

    @Override
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

    @Override
    public List<Request> sort(Book book, Comparator<Request> comparator) {
        List<Request> requests = book.getCommonRequests();
        requests.sort(comparator);
        return requests;
    }

    @Override
    public List<Request> getAll() {
        return requestRepository.findAll();
    }

    @Override
    public void saveAll(List<Request> requestList) {
        requestRepository.saveAll(requestList);
    }

    @Override
    public Request getById(long id) {
        return requestRepository.getById(id);
    }
}
