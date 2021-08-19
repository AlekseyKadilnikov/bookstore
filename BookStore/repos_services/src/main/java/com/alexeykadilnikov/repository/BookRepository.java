package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookRepository implements IBookRepository {
//    private static BookRepository instance;

    private List<Book> books = new ArrayList<>(Arrays.asList(
            new Book("Идиот", "Федор Достоевский", "Наука", 1990, 350, 0),
            new Book("Бесы", "Федор Достоевский", "Наука", 1988, 370, 0),
            new Book("Война и мир", "Лев Толстой", "Наука", 2009, 400,0),
            new Book("Палач", "Эдуард Лимонов", "Питер", 2019, 550, 0),
            new Book("Черный обелиск", "Мария Ремарк", "Феникс", 1993, 310, 50),
            new Book("Капитал", "Карл Маркс", "Питер", 2011, 660, 30),
            new Book("Лолита", "Владимир Набоков", "Питер", 2000, 490, 8),
            new Book("Сияние", "Стивен Кинг", "Питер", 2018, 500,11),
            new Book("1984", "Джордж Оруэлл", "Питер", 2010, 550, 10))
    );

//    private BookRepository() {
//    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Book getById(Long id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findAny()
                .orElse(null);
    }

    @Override
    public void save(Book book) {
        books.add(book);
    }

    @Override
    public void delete(Book book) {
        books.remove(book);
    }

    @Override
    public void saveAll(List<Book> all) {
        books = all;
    }

//    public static BookRepository getInstance() {
//        if(instance == null) {
//            instance = new BookRepository();
//        }
//        return instance;
//    }

    public void addRequest(Request request, int count, long id) {
        Book book = getById(id);
        List<Request> commonRequests = book.getCommonRequests();
        Request[] orderRequests = book.getOrderRequests();
        if(request.getStatus() == RequestStatus.COMMON) {
            for(Request r : commonRequests) {
                if(request.getName().equals(r.getName())) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
            commonRequests.add(request);
        }
        else {
            for(Request r : orderRequests) {
                if(request.getName().equals(r.getName()) && request.getStatus() == r.getStatus()) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
        }
//        RequestRepository requestRepository = RequestRepository.getInstance();
//        requestRepository.save(request);
    }
}
