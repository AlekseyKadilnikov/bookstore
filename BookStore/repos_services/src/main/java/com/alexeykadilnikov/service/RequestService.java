package com.alexeykadilnikov.service;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.repository.IAuthorRepository;
import com.alexeykadilnikov.repository.IBookRepository;
import com.alexeykadilnikov.repository.IRequestRepository;

import java.util.*;

@Singleton
public class RequestService implements IRequestService {
    @InjectBean
    private IBookRepository bookRepository;
    @InjectBean
    private IRequestRepository requestRepository;
    @InjectBean
    private IAuthorRepository authorRepository;

    @Override
    public Set<Book> createRequest(String name, int count) {
        String[] words = name.split(" ");
        List<Book> books = bookRepository.findAll();
        Set<Book> booksByAuthor = new HashSet<>();
        Set<Book> booksByName = new HashSet<>();
        for(String word : words) {
            for(Book book : books) {
                String authors = getFullAuthorStringListForBook(book);
                if(book.getName().toLowerCase().contains(word.toLowerCase())) {
                    booksByName.add(book);
                }
                else if(authors.toLowerCase().contains(word.toLowerCase())) {
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
                bookRepository.addRequest(request, count, book);
                requestRepository.save(request);
            }
            return booksByName;
        }
        else if(!booksByAuthor.isEmpty() && booksByName.isEmpty()){
            for(Book book : booksByAuthor) {
                bookIdSet.add(book.getId());
            }
            Request request = new Request(name, bookIdSet);
            for(Book book : booksByAuthor) {
                bookRepository.addRequest(request, count, book);
                requestRepository.save(request);
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
                bookRepository.addRequest(request, count, book);
                requestRepository.save(request);
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

    private String getFullAuthorStringListForBook(Book book) {
        StringBuilder authors = new StringBuilder();
        List<Long> authorsId = book.getAuthors();
        for (int i = 0; i < authorsId.size(); i++) {
            Author author = authorRepository.getById(authorsId.get(i));
            authors.append(author.getFirstName()).append(" ").append(author.getLastName()).append(" ").append(author.getMiddleName());
            if(i != authorsId.size() - 1) {
                authors.append(", ");
            }
        }
        return authors.toString();
    }
}
