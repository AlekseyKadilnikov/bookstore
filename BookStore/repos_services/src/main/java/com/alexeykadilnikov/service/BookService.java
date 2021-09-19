package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.dao.IBookDAO;
import com.alexeykadilnikov.dao.IRequestDAO;
import com.alexeykadilnikov.mapper.BookMapper;
import com.alexeykadilnikov.utils.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final IBookDAO bookDAO;
    private final IRequestDAO requestDAO;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(IBookDAO bookDAO, IRequestDAO requestDAO, BookMapper bookMapper) {
        this.bookDAO = bookDAO;
        this.requestDAO = requestDAO;
        this.bookMapper = bookMapper;
    }

    @Value("${BookService.doSuccess}")
    private boolean doSuccess;
    @Value("${BookService.months}")
    private int months;

    public BookDto addBook(long id, int bookCount) {
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
            return bookMapper.toDto(book);
        }

        if(doSuccess) {
            createSuccessRequests(bookCount, book, newRequest, successRequest);
        }

        return bookMapper.toDto(book);
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

    public List<BookDto> getAll() {
        List<Book> books = bookDAO.findAll();
        List<BookDto> booksDto = new ArrayList<>();
        for(Book book : books) {
            booksDto.add(bookMapper.toDto(book));
        }
        return booksDto;
    }

    public void createBook(Book book) {
        bookDAO.save(book);
    }

    public String getDescription(Long bookId) {
        BookDto book = getById(bookId);
        return book.getDescription();
    }

    public BookDto getById(long id) {
        Book book = bookDAO.getById(id);
        if(book == null) {
            throw new NullPointerException("Book with id = " + id + " not found");
        }
        return bookMapper.toDto(book);
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

    public BookDto writeOff(long bookId) {
        Book book = bookDAO.getById(bookId);
        book.setCount(0);
        bookDAO.update(book);
        return bookMapper.toDto(book);
    }

    public List<Book> sendSqlQuery(String hql) {
        return bookDAO.findAll(hql);
    }

    public List<Book> sort(List<Book> books, Comparator<Book> comparator) {
        books.sort(comparator);
        return books;
    }

    public List<BookDto> sortBy(String sortBy, int mode) {
        String hql = "";
        switch (sortBy) {
            case "name":
                hql = QueryBuilder.sortBooksByName(mode);
                break;
            case "price":
                hql = QueryBuilder.sortBooksByPrice(mode);
                break;
            case "year":
                hql = QueryBuilder.sortBooksByPublicationYear(mode);
                break;
            case "count":
                hql = QueryBuilder.sortBooksByCount(mode);
                break;
            default:
                return new ArrayList<>();
        }
        List<Book> books = sendSqlQuery(hql);
        List<BookDto> booksDto = new ArrayList<>();
        for(Book book : books) {
            booksDto.add(bookMapper.toDto(book));
        }
        return booksDto;
    }

    public List<BookDto> getStaleBooks(String sortBy, int mode) {
        String hql = "";
        switch (sortBy) {
            case "name":
                hql = QueryBuilder.getStaleBooksByDate(mode, months);
                break;
            case "price":
                hql = QueryBuilder.getStaleBooksByPrice(mode, months);
                break;
            default:
                return new ArrayList<>();
        }

        List<Book> books = sendSqlQuery(hql);
        List<BookDto> booksDto = new ArrayList<>();
        for(Book book : books) {
            booksDto.add(bookMapper.toDto(book));
        }
        return booksDto;
    }
}
