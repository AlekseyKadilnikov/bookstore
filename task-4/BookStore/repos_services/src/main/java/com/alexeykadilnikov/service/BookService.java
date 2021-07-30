package com.alexeykadilnikov.service;

import com.alexeykadilnikov.comparator.BookComparator;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.RequestStatus;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.RequestRepository;

import java.util.Arrays;

public class BookService implements IBookService {
    private final BookRepository bookRepository;
    private final RequestRepository requestRepository;

    public BookService(BookRepository bookRepository, RequestRepository requestRepository) {
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void setBookStatus(int index, boolean status) {
        if(!bookRepository.getByIndex(index).isAvailable() && status) {

            requestRepository.getByIndex(0).setStatus(RequestStatus.Closed);
            System.out.println("Request id = " + requestRepository.getByIndex(0).getId() + " closed");
        }
        bookRepository.getByIndex(index).setAvailable(status);
    }

    @Override
    public String showBook(int index) {
        return bookRepository.getByIndex(index).toString();
    }

    public Book getByIndex(int index) {
        return bookRepository.getByIndex(index);
    }

    public Book[] sortByNameAscending(Book[] books) {
        Arrays.sort(books, BookComparator.NameComparatorAscending);
        return books;
    }

    public Book[] sortByNameDescending(Book[] books) {
        Arrays.sort(books, BookComparator.NameComparatorDescending);
        return books;
    }

    public Book[] sortByPriceAscending(Book[] books) {
        Arrays.sort(books, BookComparator.PriceComparatorAscending);
        return books;
    }

    public Book[] sortByPriceDescending(Book[] books) {
        Arrays.sort(books, BookComparator.PriceComparatorDescending);
        return books;
    }

    public Book[] sortByYearAscending(Book[] books) {
        Arrays.sort(books, BookComparator.DateComparatorAscending);
        return books;
    }

    public Book[] sortByYearDescending(Book[] books) {
        Arrays.sort(books, BookComparator.DateComparatorDescending);
        return books;
    }

    public Book[] sortByAvailableAscending(Book[] books) {
        Arrays.sort(books, BookComparator.AvailableComparatorAscending);
        return books;
    }

    public Book[] sortByAvailableDescending(Book[] books) {
        Arrays.sort(books, BookComparator.AvailableComparatorDescending);
        return books;
    }
}
