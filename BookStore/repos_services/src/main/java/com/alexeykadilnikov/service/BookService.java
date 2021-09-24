package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.mapper.BookMapper;
import com.alexeykadilnikov.repository.IBookRepository;
import com.alexeykadilnikov.repository.IRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final IBookRepository bookRepository;
    private final IRequestRepository requestRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(IBookRepository bookRepository, IRequestRepository requestRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
        this.bookMapper = bookMapper;
    }

    @Value("${BookService.doSuccess}")
    private boolean doSuccess;
    @Value("${BookService.months}")
    private int months;

    @Transactional
    public BookDto addBook(long id, int bookCount) {
        Book book = bookRepository.getById(id);
        Request newRequest = book.getRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.NEW)
                .findFirst().orElse(null);
        Request successRequest = book.getRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.SUCCESS)
                .findFirst().orElse(null);
        if(book.getCount() > 0 || (book.getCount() == 0 && newRequest == null) || newRequest == null) {
            book.setCount(book.getCount() + bookCount);
            bookRepository.save(book);
            return bookMapper.toDto(book);
        }

        if(doSuccess) {
            createSuccessRequests(bookCount, book, newRequest, successRequest);
        }

        return bookMapper.toDto(book);
    }

    @Transactional
    public void createSuccessRequests(int bookCount, Book book, Request newRequest, Request successRequest) {
        Set<Book> books = new HashSet<>();
        books.add(book);
        int diff = bookCount - newRequest.getCount();
        if(diff >= 0) {
            if(successRequest == null) {
                successRequest = new Request(newRequest.getName(), newRequest.getCount(), RequestStatus.SUCCESS, books);
            } else {
                successRequest.setCount(successRequest.getCount() + newRequest.getCount());
            }
            requestRepository.save(successRequest);
            requestRepository.delete(newRequest);
            book.setCount(diff);
            bookRepository.save(book);
        } else {
            if (successRequest == null) {
                successRequest = new Request(newRequest.getName(), bookCount, RequestStatus.SUCCESS, books);
            } else {
                successRequest.setCount(successRequest.getCount() + bookCount);
            }
            requestRepository.save(successRequest);
            newRequest.setCount(newRequest.getCount() - bookCount);
            requestRepository.save(newRequest);
        }
    }

    public List<BookDto> getAll() {
        List<Book> books = bookRepository.findAll();
        List<BookDto> booksDto = new ArrayList<>();
        for(Book book : books) {
            booksDto.add(bookMapper.toDto(book));
        }
        return booksDto;
    }

    public void createBook(Book book) {
        bookRepository.save(book);
    }


    public String getDescription(Long bookId) {
        BookDto book = getById(bookId);
        return book.getDescription();
    }

    public BookDto getById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty()) {
            throw new NullPointerException("Book with id = " + id + " not found");
        }
        return bookMapper.toDto(book.get());
    }

    @Transactional
    public void createRequest(Request request, long id) {
        Book book = bookRepository.getById(id);
        book.getRequests().add(request);
        requestRepository.save(request);
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

    @Transactional
    public BookDto writeOff(long bookId) {
        Book book = bookRepository.getById(bookId);
        book.setCount(0);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    public List<Book> sort(List<Book> books, Comparator<Book> comparator) {
        books.sort(comparator);
        return books;
    }

    public List<BookDto> sortBy(String sortBy, String direction) {
        List<Book> sorted;
        switch (sortBy) {
            case "name":
                if(direction.equalsIgnoreCase("asc")) {
                    sorted = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
                } else {
                    sorted = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
                }
                break;
            case "price":
                if(direction.equalsIgnoreCase("asc")) {
                    sorted = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
                } else {
                    sorted = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
                }
                break;
            case "year":
                if(direction.equalsIgnoreCase("asc")) {
                    sorted = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "publicationYear"));
                } else {
                    sorted = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "publicationYear"));
                }
                break;
            case "count":
                if(direction.equalsIgnoreCase("asc")) {
                    sorted = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "count"));
                } else {
                    sorted = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "count"));
                }
                break;
            default:
                return new ArrayList<>();
        }

        List<BookDto> booksDto = new ArrayList<>();
        for(Book book : sorted) {
            booksDto.add(bookMapper.toDto(book));
        }
        return booksDto;
    }

    public List<BookDto> getStaleBooks(String sortBy, String direction) {
        List<Book> sorted;
        LocalDate thresholdDate = LocalDate.now().minusMonths(months);
        switch (sortBy) {
            case "name":
                if(direction.equalsIgnoreCase("asc")) {
                    sorted = bookRepository.getStateBooksOrderByNameAsc(thresholdDate);
                } else {
                    sorted = bookRepository.getStateBooksOrderByNameDesc(thresholdDate);
                }
                break;
            case "price":
                if(direction.equalsIgnoreCase("asc")) {
                    sorted = bookRepository.getStateBooksOrderByPriceAsc(thresholdDate);
                } else {
                    sorted = bookRepository.getStateBooksOrderByPriceDesc(thresholdDate);
                }
                break;
            default:
                return new ArrayList<>();
        }

        List<BookDto> booksDto = new ArrayList<>();
        for(Book book : sorted) {
            booksDto.add(bookMapper.toDto(book));
        }
        return booksDto;
    }
}
