package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.dao.IBookDAO;
import com.alexeykadilnikov.dao.IRequestDAO;
import com.alexeykadilnikov.utils.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final IBookDAO bookDAO;
    private final IRequestDAO requestDAO;

    @Autowired
    public BookService(IBookDAO bookDAO, IRequestDAO requestDAO) {
        this.bookDAO = bookDAO;
        this.requestDAO = requestDAO;
    }

    @Value("${BookService.doSuccess}")
    private boolean doSuccess;
    @Value("${BookService.months}")
    private int months;

    public void saveAll(List<Book> bookList) {
        for(Book book : bookList) {
            bookDAO.save(book);
        }
    }

    public void addBook(long id, int bookCount) {
        Book book = bookDAO.getById(id);
        Request newRequest = book.getRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.NEW)
                .findFirst().orElse(null);
        Request successRequest = book.getRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.SUCCESS)
                .findFirst().orElse(null);
        if(book.getCount() > 0 || (book.getCount() == 0 && newRequest == null) || newRequest == null) {
            book.setCount(book.getCount() + bookCount);
            bookDAO.update(book);
            return;
        }

        if(doSuccess) {
            createSuccessRequests(bookCount, book, newRequest, successRequest);
        }
    }

    private void createSuccessRequests(int bookCount, Book book, Request newRequest, Request successRequest) {
        Set<Book> books = new HashSet<>();
        books.add(book);
        int diff = bookCount - newRequest.getCount();
        if(diff >= 0) {
            if(successRequest == null) {
                successRequest = new Request(newRequest.getName(), newRequest.getCount(), RequestStatus.SUCCESS, books);
                requestDAO.save(successRequest);
            } else {
                successRequest.setCount(successRequest.getCount() + newRequest.getCount());
                requestDAO.update(successRequest);
            }
            requestDAO.delete(newRequest);
            book.setCount(diff);
            bookDAO.update(book);
        } else {
            if (successRequest == null) {
                successRequest = new Request(newRequest.getName(), bookCount, RequestStatus.SUCCESS, books);
                requestDAO.save(successRequest);
            } else {
                successRequest.setCount(successRequest.getCount() + bookCount);
                requestDAO.update(successRequest);
            }
            newRequest.setCount(newRequest.getCount() - bookCount);
            requestDAO.update(newRequest);
        }
    }

    public String showBook(long id) {
        return bookDAO.getById(id).toString();
    }

    public List<Book> getAll() {
        return bookDAO.findAll();
    }

    public void createBook(Book book) {
        bookDAO.save(book);
    }

    public String getDescription(Long bookId) {
        Book book = getById(bookId);
        if(book != null) {
            System.out.println(book.getDescription());
        } else {
            System.out.println("Book with id = " + bookId + " does not exist!");
        }
        return book.getDescription();
    }

    public Book getById(long id) {
        return bookDAO.getById(id);
    }

    public void createRequest(Request request, long id) {
        Book book = bookDAO.getById(id);
        book.getRequests().add(request);
        requestDAO.save(request);
    }

    public List<Book> getOldBooks(int monthsAmount) {
        LocalDate date = LocalDate.now().minusMonths(monthsAmount);
        List<Book> books = new ArrayList<>();
        for(Book book : bookDAO.findAll()) {
            if(book.getDateOfReceipt().isBefore(date)) {
                books.add(book);
            }
        }
        return books;
    }

    public void writeOff(long bookId) {
        Book book = bookDAO.getById(bookId);
        book.setCount(0);
        bookDAO.update(book);
    }

    public List<Book> sendSqlQuery(String hql) {
        return bookDAO.findAll(hql);
    }

    public List<Book> sort(List<Book> books, Comparator<Book> comparator) {
        books.sort(comparator);
        return books;
    }
    public void sortByName(int mode) {
        String hql = QueryBuilder.sortBooksByName(mode);

        List<Book> books = sendSqlQuery(hql);

        System.out.println(books);
    }

    public void sortByPrice(int mode) {
        String hql = QueryBuilder.sortBooksByPrice(mode);

        List<Book> books = sendSqlQuery(hql);

        System.out.println(books);
    }

    public void sortByPublicationYear(int mode) {
        String hql = QueryBuilder.sortBooksByPublicationYear(mode);

        List<Book> books = sendSqlQuery(hql);

        System.out.println(books);
    }

    public void sortByCount(int mode) {
        String hql = QueryBuilder.sortBooksByCount(mode);

        List<Book> books = sendSqlQuery(hql);

        System.out.println(books);
    }

    public void getStaleBooksByDate(int mode) {
        String hql = QueryBuilder.getStaleBooksByDate(mode, months);

        List<Book> books = sendSqlQuery(hql);

        System.out.println(books);
    }

    public void getStaleBooksByPrice(int mode) {
        String hql = QueryBuilder.getStaleBooksByPrice(mode, months);

        List<Book> books = sendSqlQuery(hql);

        System.out.println(books);
    }
}
