package com.alexeykadilnikov.service;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.ConfigProperty;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.dao.IBookDAO;
import com.alexeykadilnikov.dao.IRequestDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

@Singleton
public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @InjectBean
    private IBookDAO bookDAO;
    @InjectBean
    private IRequestDAO requestDAO;

    @ConfigProperty(configName = "properties\\bookstore.yml", propertyName = "BookService.doSuccess", type = boolean.class)
    private boolean doSuccess;

    @Override
    public void saveAll(List<Book> bookList) {
        for(Book book : bookList) {
            bookDAO.save(book);
        }
    }

    @Override
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
    }

    @Override
    public String showBook(long id) {
        return bookDAO.getById(id).toString();
    }

    @Override
    public List<Book> getAll() {
        return bookDAO.findAll();
    }

    @Override
    public void createBook(Book book) {
        bookDAO.save(book);
    }

    @Override
    public String getBookDescription(Book book) {
        return book.getDescription();
    }

    @Override
    public Book getById(long id) {
        return bookDAO.getById(id);
    }

    @Override
    public void createRequest(Request request, long id) {
        Book book = bookDAO.getById(id);
        book.getRequests().add(request);
        requestDAO.save(request);
    }

    @Override
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

    @Override
    public void writeOff(long bookId) {
        Book book = bookDAO.getById(bookId);
        book.setCount(0);
        bookDAO.update(book);
    }

    @Override
    public List<Book> sendSqlQuery(String hql) {
        return bookDAO.findAll(hql);
    }

    @Override
    public List<Book> sort(List<Book> books, Comparator<Book> comparator) {
        books.sort(comparator);
        return books;
    }
}
