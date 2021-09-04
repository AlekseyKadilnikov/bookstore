package com.alexeykadilnikov.service;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.RequestStatus;
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

        return getBooksByRequest(name, count, booksByAuthor, booksByName);
    }

    @Override
    public List<Request> sort(Book book, Comparator<Request> comparator) {
        List<Request> requests = requestRepository.findAll();
        requests.sort(comparator);
        return requests;
    }

    @Override
    public List<Request> getAll() {
        return requestRepository.findAll();
    }

    @Override
    public void saveAll(List<Request> requestList) {
        for(Request request : requestList) {
            requestRepository.save(request);
        }
    }

    @Override
    public Request getById(long id) {
        return requestRepository.getById(id);
    }

    private Set<Book> getBooksByRequest(String name, int count, Set<Book> booksByAuthor, Set<Book> booksByName) {
        Set<Book> bookSet = new HashSet<>();
        if(booksByAuthor.isEmpty() && !booksByName.isEmpty()) {
            bookSet.addAll(booksByName);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            for(Book book : booksByName) {
                book.getRequests().add(request);
                requestRepository.save(request);
            }
            return booksByName;
        }
        else if(!booksByAuthor.isEmpty() && booksByName.isEmpty()){
            bookSet.addAll(booksByAuthor);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            for(Book book : booksByAuthor) {
                book.getRequests().add(request);
                requestRepository.save(request);
            }
            return booksByAuthor;
        }
        else {
            booksByAuthor.retainAll(booksByName);
            bookSet.addAll(booksByAuthor);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            for(Book book : booksByAuthor) {
                book.getRequests().add(request);
                requestRepository.save(request);
            }
            return booksByAuthor;
        }
    }

    private String getFullAuthorStringListForBook(Book book) {
        StringBuilder authorsStr = new StringBuilder();
        Set<Author> authors = book.getAuthors();
        for (int i = 0; i < authors.size(); i++) {
            Author author = authors.iterator().next();
            authorsStr.append(author.getFirstName())
                    .append(" ").append(author.getLastName())
                    .append(" ").append(author.getMiddleName());
            if(i != authors.size() - 1) {
                authorsStr.append(", ");
            }
        }
        return authorsStr.toString();
    }
}
