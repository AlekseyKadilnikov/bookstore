package com.alexeykadilnikov.service;

import com.alexeykadilnikov.BookComparator;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.repository.BookRepository;

import java.time.LocalDate;
import java.util.*;

public class BookService implements IBookService {
    private static BookService instance;

    private final BookRepository bookRepository;

    private BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(int index, int count) {
        Book book = bookRepository.getByIndex(index);
        book.setCount(book.getCount() + count);
        List<Request> requests = book.getOrderRequests();
        for(Request request : requests) {
            if(request.getStatus() == RequestStatus.NEW) {;
                book.addRequest(new Request(request.getName(), RequestStatus.SUCCESS));
                if(request.getCount() > 0) {
                    request.setCount(request.getCount() - 1);
                }
                else {
                    requests.remove(request);
                }
            }
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

    public String getBookDescription(Book book) {
        return book.getDescription();
    }

    public Book getByIndex(int index) {
        return bookRepository.getByIndex(index);
    }

    public Book getById(long id) {
        return bookRepository.getById(id);
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
