package com.alexeykadilnikov;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.RequestRepository;
import com.alexeykadilnikov.service.BookService;

public class Test {
    public static void main(String[] args) {
        RequestRepository requestRepository = new RequestRepository();
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository, requestRepository);

        Book[] books = bookService.sortByNameAscending();
        showBookArray(books);
        books = bookService.sortByNameDescending();
        showBookArray(books);

        books = bookService.sortByPriceAscending();
        showBookArray(books);
        books = bookService.sortByPriceDescending();
        showBookArray(books);

        books = bookService.sortByYearAscending();
        showBookArray(books);
        books = bookService.sortByYearDescending();
        showBookArray(books);

        books = bookService.sortByAvailableAscending();
        showBookArray(books);
        books = bookService.sortByAvailableDescending();
        showBookArray(books);
    }

    private static void showBookArray(Book[] books) {
        for (Book book : books) {
            System.out.println("name = " + book.getName() +
                    ", price = " + book.getPrice() +
                    ", date = " + book.getPublicationYear() +
                    ", available = " + book.isAvailable());
        }
        System.out.println();
    }

    private static void showOrderArray(Order[] orders) {
        for (Order order : orders) {
            System.out.println("id = " + order.getId() +
                    ", price = " + order.getPrice() +
                    ", date = " + order.getExecutionDate() +
                    ", status = " + order.getStatus());
        }
        System.out.println();
    }
}
