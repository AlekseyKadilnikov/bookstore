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
        Request[] requests = book.getOrderRequests();

        if(book.getCount() > 0 || (book.getCount() == 0 && requests[0] == null)) {
            book.setCount(book.getCount() + bookCount);
            bookRepository.update(book);
            return;
        }

        if(doSuccess) {
            Set<Long> booksId = new HashSet<>();
            booksId.add(book.getId());
            Request successRequest = new Request(requests[0].getName(), booksId, requests[0].getOrdersId(), RequestStatus.SUCCESS);
            int diff = bookCount - requests[0].getCount();
            if(diff >= 0) {
                bookRepository.addRequest(successRequest, requests[0].getCount(), book);
                requestRepository.save(successRequest);
                requests[0].setCount(0);
                requests[0].getOrdersId().clear();
                book.setCount(diff);
                requestRepository.delete(requests[0]);
                bookRepository.update(book);
            } else {
                bookRepository.addRequest(successRequest, bookCount, book);
                requestRepository.save(successRequest);
                requests[0].setCount(requests[0].getCount() - bookCount);
                requestRepository.update(requests[0]);
            }
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
    public void writeOff(long bookId) {
        Book book = bookRepository.getById(bookId);
        book.setCount(0);
        bookRepository.update(book);
    }

    @Override
    public List<Book> sort(List<Book> books, Comparator<Book> comparator) {
        books.sort(comparator);
        return books;
    }
}
