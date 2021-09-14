package com.alexeykadilnikov.service;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.dao.IBookDAO;
import com.alexeykadilnikov.dao.IRequestDAO;
import com.alexeykadilnikov.utils.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RequestService implements IRequestService {
    private final IBookDAO bookDAO;
    private final  IRequestDAO requestDAO;

    @Autowired
    public RequestService(IBookDAO bookDAO, IRequestDAO requestDAO) {
        this.bookDAO = bookDAO;
        this.requestDAO = requestDAO;
    }

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

    public List<Request> sort(Book book, Comparator<Request> comparator) {
        List<Request> requests = requestDAO.findAll();
        requests.sort(comparator);
        return requests;
    }

    public List<Request> getAll() {
        return requestDAO.findAll();
    }

    public void saveAll(List<Request> requestList) {
        for(Request request : requestList) {
            requestDAO.save(request);
        }
    }

    public Request getById(long id) {
        return requestDAO.getById(id);
    }

    public List<Request> sendSqlQuery(String hql) {
        return requestDAO.findAll(hql);
    }

    public void getRequestsForBookSortedByCount(long bookId, int mode) {
        String hql = QueryBuilder.getRequestsForBookSortedByCount(bookId, mode);

        List<Request> requests = sendSqlQuery(hql);

        System.out.println(requests);
    }

    public void getRequestsForBookSortedByName(long bookId, int mode) {
        String hql = QueryBuilder.getRequestsForBookSortedByName(bookId, mode);

        List<Request> requests = sendSqlQuery(hql);

        System.out.println(requests);
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
