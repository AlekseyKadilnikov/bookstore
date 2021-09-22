package com.alexeykadilnikov.service;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.dto.RequestDto;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.mapper.RequestMapper;
import com.alexeykadilnikov.repository.IBookRepository;
import com.alexeykadilnikov.repository.IRequestRepository;
import com.alexeykadilnikov.utils.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RequestService implements IRequestService {
    private final IBookRepository bookRepository;
    private final IRequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Autowired
    public RequestService(IBookRepository bookRepository, IRequestRepository requestRepository, RequestMapper requestMapper) {
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    public Set<Book> createRequest(String name, int count) {
        Request request = requestRepository.findAll()
                .stream()
                .filter(r -> r.getName().equals(name))
                .findAny()
                .orElse(null);
        if(request != null) {
            request.setCount(request.getCount() + 1);
            requestRepository.save(request);
            return request.getBooks();
        }

        String[] words = name.split(" ");
        List<Book> books = bookRepository.findAll();
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
        List<Request> requests = requestRepository.findAll();
        requests.sort(comparator);
        return requests;
    }

    public List<RequestDto> getAll() {
        List<Request> requests = requestRepository.findAll();
        List<RequestDto> requestsDto = new ArrayList<>();
        for(Request request : requests) {
            RequestDto requestDto = requestMapper.toDto(request);
            requestsDto.add(requestDto);
        }
        return requestsDto;
    }

    public RequestDto save(RequestDto requestDto) {
        Request request = requestMapper.toEntity(requestDto);
        request.setCount(1);
        createRequest(request.getName(), request.getCount());
        return requestMapper.toDto(request);
    }

    public RequestDto getById(long id) {
        Request request = requestRepository.getById(id);
        if(request == null) {
            throw new NullPointerException("Request with id = " + id + " not found");
        }
        return requestMapper.toDto(request);
    }

    public List<Request> sendSqlQuery(String hql) {
        return requestRepository.findAll();
    }

    public List<RequestDto> getRequestsForBook(long bookId, String sortBy, int mode) {
        String hql = "";
        switch (sortBy) {
            case "count":
                hql = QueryBuilder.getRequestsForBookSortedByCount(bookId, mode);
                break;
            case "name":
                hql = QueryBuilder.getRequestsForBookSortedByName(bookId, mode);
                break;
            default:
                return new ArrayList<>();
        }

        List<Request> requests = sendSqlQuery(hql);
        List<RequestDto> requestsDto = new ArrayList<>();
        for(Request request : requests) {
            RequestDto requestDto = requestMapper.toDto(request);
            requestsDto.add(requestDto);
        }

        return requestsDto;
    }

    private Set<Book> getBooksByRequest(String name, int count, Set<Book> booksByAuthor, Set<Book> booksByName) {
        Set<Book> bookSet = new HashSet<>();
        if(booksByAuthor.isEmpty() && !booksByName.isEmpty()) {
            bookSet.addAll(booksByName);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            requestRepository.save(request);
            return booksByName;
        }
        else if(!booksByAuthor.isEmpty() && booksByName.isEmpty()){
            bookSet.addAll(booksByAuthor);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            requestRepository.save(request);
            return booksByAuthor;
        }
        else {
            booksByAuthor.retainAll(booksByName);
            bookSet.addAll(booksByAuthor);
            Request request = new Request(name, count, RequestStatus.COMMON, bookSet);
            requestRepository.save(request);
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
