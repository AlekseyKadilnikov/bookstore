package com.alexeykadilnikov.service;

import com.alexeykadilnikov.annotation.InjectBean;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.repository.IBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

//    private static BookService instance;

    @InjectBean
    private IBookRepository bookRepository;

//    private BookService() {
//    }
//
//    public static BookService getInstance() {
//        if(instance == null) {
//            instance = new BookService();
//        }
//        return instance;
//    }

    @Override
    public void saveAll(List<Book> bookList) {
        bookRepository.saveAll(bookList);
    }

    @Override
    public void addBook(long id, int bookCount) {
        boolean doSuccess = true;
        try(
                FileInputStream fis = new FileInputStream("properties\\bookstore.yml");
                ) {
            Properties property = new Properties();
            property.load(fis);
            doSuccess = Boolean.getBoolean(property.getProperty("successRequests").trim());
        } catch (IOException e) {
            logger.error("File bookstore.yml not found!");
        }

        Book book = bookRepository.getById(id);
        if(doSuccess) {
            Request[] requests = book.getOrderRequests();
            for(Request request : requests) {
                if(request.getStatus() == RequestStatus.NEW) {
                    Request r = new Request(request.getName(), book.getId(), request.getOrdersId(), RequestStatus.SUCCESS);
                    int diff = bookCount - request.getCount();
                    if(diff >= 0) {
                        bookRepository.addRequest(r, request.getCount(), book.getId());
                        request.setCount(0);
                        request.getOrdersId().clear();
                        book.setCount(diff);
                    } else {
                        bookRepository.addRequest(r, bookCount, book.getId());
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
        bookRepository.addRequest(request, count, id);
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
