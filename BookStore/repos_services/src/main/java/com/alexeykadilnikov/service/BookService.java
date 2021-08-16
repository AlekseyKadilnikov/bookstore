package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.repository.BookRepository;
import net.sf.saxon.trans.SymbolicName;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class BookService implements IBookService {
    private static BookService instance;

    private final BookRepository bookRepository;

    private BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(int index, int bookCount) {
        boolean doSuccess = true;
        try(
                FileInputStream fis = new FileInputStream("src\\main\\properties\\bookstore.yml");
                ) {
            Properties property = new Properties();
            property.load(fis);
            doSuccess = Boolean.getBoolean(property.getProperty("successRequests").trim());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Book book = bookRepository.getByIndex(index);
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
    public String showBook(int index) {
        return bookRepository.getByIndex(index).toString();
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public static BookService getInstance() {
        if(instance == null) {
            instance = new BookService(BookRepository.getInstance());
        }
        return instance;
    }

    public void createBook(Book book) {
        bookRepository.save(book);
    }

    public String getBookDescription(Book book) {
        return book.getDescription();
    }

    public Book getByIndex(int index) {
        return bookRepository.getByIndex(index);
    }

    public Book getById(long id) {
        return bookRepository.getById(id);
    }

    public void createRequest(Request request, int count, long id) {
        bookRepository.addRequest(request, count, id);
    }

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

    public List<Book> sort(List<Book> books, Comparator<Book> comparator) {
        books.sort(comparator);
        return books;
    }
}
