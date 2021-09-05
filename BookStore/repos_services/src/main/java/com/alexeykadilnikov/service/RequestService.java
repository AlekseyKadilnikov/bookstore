package com.alexeykadilnikov.service;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.dao.IAuthorDAO;
import com.alexeykadilnikov.dao.IBookDAO;
import com.alexeykadilnikov.dao.IRequestDAO;

import java.util.*;

@Singleton
public class RequestService implements IRequestService {
    @InjectBean
    private IBookDAO bookDAO;
    @InjectBean
    private IRequestDAO requestDAO;
    @InjectBean
    private IAuthorDAO authorDAO;

    @Override
    public Set<Book> createRequest(String name, int count) {
        Request request = requestDAO.findAll()
                .stream()
                .filter(r -> r.getName().equals(name))
                .findAny()
                .orElse(null);
        if(request != null) {
            request.setCount(request.getCount() + 1);
            requestDAO.update(request);
            return request.getBooks();
        }

        String[] words = name.split(" ");
        List<Book> books = bookDAO.findAll();
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
        List<Request> requests = requestDAO.findAll();
        requests.sort(comparator);
        return requests;
    }

    @Override
    public List<Request> getAll() {
        return requestDAO.findAll();
    }

    @Override
    public void saveAll(List<Request> requestList) {
        for(Request request : requestList) {
            requestDAO.save(request);
        }
    }

    @Override
    public Request getById(long id) {
        return requestDAO.getById(id);
    }

    private Set<Book> getBooksByRequest(String name, int count, Set<Book> booksByAuthor, Set<Book> booksByName) {
        Set<Book> bookSet = new HashSet<>();
        if(booksByAuthor.isEmpty() && !booksByName.isEmpty()) {
            bookSet.addAll(booksByName);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            requestDAO.save(request);
            return booksByName;
        }
        else if(!booksByAuthor.isEmpty() && booksByName.isEmpty()){
            bookSet.addAll(booksByAuthor);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            requestDAO.save(request);
            return booksByAuthor;
        }
        else {
            booksByAuthor.retainAll(booksByName);
            bookSet.addAll(booksByAuthor);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            requestDAO.save(request);
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
