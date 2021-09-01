package com.alexeykadilnikov.service;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.ConfigProperty;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.repository.IBookRepository;
import com.alexeykadilnikov.repository.IRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

@Singleton
public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @InjectBean
    private IBookRepository bookRepository;
    @InjectBean
    private IRequestRepository requestRepository;

    @ConfigProperty(configName = "properties\\bookstore.yml", propertyName = "BookService.doSuccess", type = boolean.class)
    private boolean doSuccess;

    @Override
    public void saveAll(List<Book> bookList) {
        bookRepository.saveAll(bookList);
    }

    @Override
    public void addBook(long id, int bookCount) {
        Book book = bookRepository.getById(id);
        if(doSuccess) {
            Request[] requests = book.getOrderRequests();
            for(Request request : requests) {
                if(request.getStatus() == RequestStatus.NEW) {
                    Set<Long> booksId = new HashSet<>();
                    booksId.add(book.getId());
                    Request r = new Request(request.getName(), booksId, request.getOrdersId(), RequestStatus.SUCCESS);
                    int diff = bookCount - request.getCount();
                    if(diff >= 0) {
                        bookRepository.addRequest(r, request.getCount(), book);
                        request.setCount(0);
                        request.getOrdersId().clear();
                        book.setCount(diff);
                    } else {
                        bookRepository.addRequest(r, bookCount, book);
                        request.setCount(request.getCount() - bookCount);
                    }
                }
            }
        } else {
            book.setCount(book.getCount() + bookCount);
        }
    }

    @Override
    public String showBook(long id) {
        return bookRepository.getById(id).toString();
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public void createBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public String getBookDescription(Book book) {
        return book.getDescription();
    }

    @Override
    public Book getById(long id) {
        return bookRepository.getById(id);
    }

    @Override
    public void createRequest(Request request, int count, long id) {
        Book book = bookRepository.getById(id);
        bookRepository.addRequest(request, count, book);
    }

    @Override
    public List<Book> getOldBooks(int monthsAmount) {
        LocalDate date = LocalDate.now().minusMonths(monthsAmount);
        List<Book> books = new ArrayList<>();
        for(Book book : bookRepository.findAll()) {
            if(book.getDateOfReceipt().isBefore(date)) {
                books.add(book);
            }
        }
        return books;
    }

    @Override
    public List<Book> sort(List<Book> books, Comparator<Book> comparator) {
        books.sort(comparator);
        return books;
    }
}
