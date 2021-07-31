package com.alexeykadilnikov.service;

import com.alexeykadilnikov.BookComparator;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.RequestRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

            requestRepository.getByIndex(0).setStatus(RequestStatus.CLOSED);
            System.out.println("Request id = " + requestRepository.getByIndex(0).getId() + " closed");
        }
        bookRepository.getByIndex(index).setAvailable(status);
    }

    @Override
    public String showBook(int index) {
        return bookRepository.getByIndex(index).toString();
    }

    @Override
    public Book[] getAll() {
        return bookRepository.findAll();
    }

    public String getBookDescription(Book book) {
        return book.getDescription();
    }

    public Book getByIndex(int index) {
        return bookRepository.getByIndex(index);
    }

    public Book[] getOldBooks(int monthsAmount) {
        Calendar calendar = new GregorianCalendar();
        calendar.roll(Calendar.MONTH, monthsAmount);
        Book[] books = new Book[0];
        for(Book book : bookRepository.findAll()) {
            if(book.getDateOfReceipt().before(calendar.getTime())) {
                Book[] newBooks = new Book[books.length + 1];
                System.arraycopy(books, 0, newBooks, 0, books.length);
                newBooks[books.length] = book;
                books = newBooks;
            }
        }
        return books;
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

    public Book[] sortByDateOfReceiptAscending(Book[] books) {
        Arrays.sort(books, BookComparator.ReceiptComparatorAscending);
        return books;
    }

    public Book[] sortByDateOfReceiptDescending(Book[] books) {
        Arrays.sort(books, BookComparator.ReceiptComparatorDescending);
        return books;
    }
}
