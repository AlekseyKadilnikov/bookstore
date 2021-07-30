package com.alexeykadilnikov;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.OrderStatus;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.RequestRepository;
import com.alexeykadilnikov.repository.UserRepository;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.service.UserService;

import java.util.Date;

import static java.lang.Thread.sleep;

public class Test {
    public static void main(String[] args) {
        RequestRepository requestRepository = new RequestRepository();
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository, requestRepository);

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        userService.addUser("Alex");

        OrderRepository orderRepository = new OrderRepository();
        OrderService orderService = new OrderService(orderRepository, requestRepository);

        Book book1 = bookService.getByIndex(0);
        Book book2 = bookService.getByIndex(1);
        orderService.createOrder(new Book[] {book1}, userService.getUser(), new Date());
        orderService.createOrder(new Book[] {book1, book2}, userService.getUser(), new Date());
        orderService.createOrder(new Book[] {book1, book1}, userService.getUser(), new Date());

        Order[] orders = orderService.sortByExecutionDateDescending();
        showOrderArray(orders);

        orders = orderService.sortByPriceAscending();
        showOrderArray(orders);
        orders = orderService.sortByPriceDescending();
        showOrderArray(orders);

        orderService.setStatus(0, OrderStatus.Completed);
        orderService.setStatus(1, OrderStatus.Canceled);

        orders = orderService.sortByStatusAscending();
        showOrderArray(orders);
        orders = orderService.sortByStatusDescending();
        showOrderArray(orders);


//        Book[] books = bookService.sortByNameAscending();
//        showBookArray(books);
//        books = bookService.sortByNameDescending();
//        showBookArray(books);
//
//        books = bookService.sortByPriceAscending();
//        showBookArray(books);
//        books = bookService.sortByPriceDescending();
//        showBookArray(books);
//
//        books = bookService.sortByYearAscending();
//        showBookArray(books);
//        books = bookService.sortByYearDescending();
//        showBookArray(books);
//
//        books = bookService.sortByAvailableAscending();
//        showBookArray(books);
//        books = bookService.sortByAvailableDescending();
//        showBookArray(books);
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
