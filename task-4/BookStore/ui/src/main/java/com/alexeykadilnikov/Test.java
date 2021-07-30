package com.alexeykadilnikov;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.OrderStatus;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.RequestRepository;
import com.alexeykadilnikov.repository.UserRepository;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

        Order[] orders = orderService.sortByExecutionDateDescending(orderService.getAll());
        showOrderArray(orders);

        orders = orderService.sortByPriceAscending(orderService.getAll());
        showOrderArray(orders);
        orders = orderService.sortByPriceDescending(orderService.getAll());
        showOrderArray(orders);

        orderService.setStatus(0, OrderStatus.COMPLETED);
        orderService.setStatus(1, OrderStatus.CANCELED);

        orders = orderService.sortByStatusAscending(orderService.getAll());
        showOrderArray(orders);
        orders = orderService.sortByStatusDescending(orderService.getAll());
        showOrderArray(orders);

        orderService.createOrder(new Book[] {book1}, userService.getUser(), new Date());
        orderService.createOrder(new Book[] {book1, book2}, userService.getUser(), new Date());
        orderService.createOrder(new Book[] {book1, book1}, userService.getUser(), new Date());
        orderService.setStatus(2, OrderStatus.COMPLETED);
        orderService.setStatus(3, OrderStatus.COMPLETED);
        orderService.setStatus(4, OrderStatus.COMPLETED);
        orderService.setStatus(5, OrderStatus.COMPLETED);
        showOrderArray(orderService.getAll());
        Calendar calendarAfter = new GregorianCalendar(2021, Calendar.JULY , 30);
        Calendar calendarBefore = new GregorianCalendar(2021, Calendar.AUGUST , 3);
        orders = orderService.getOrderListForPeriod(calendarAfter.getTime(), calendarBefore.getTime());
        orders = orderService.sortByExecutionDateDescending(orders);
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
