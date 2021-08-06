package com.alexeykadilnikov;

import com.alexeykadilnikov.controller.OrderController;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.UserRepository;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.service.RequestService;
import com.alexeykadilnikov.service.UserService;
import com.alexeykadilnikov.view.menu.MenuController;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.alexeykadilnikov.utils.Utils.*;

public class Test {
    public static void main(String[] args) {
        BookRepository bookRepository = BookRepository.getInstance();
        UserRepository userRepository = UserRepository.getInstance();
        Book book = bookRepository.getByIndex(0);
        Book book1 = bookRepository.getByIndex(1);
        Book book2 = bookRepository.getByIndex(2);
        Book book3 = bookRepository.getByIndex(3);
        OrderService orderService = OrderService.getInstance();
        orderService.createOrder(Arrays.asList(book, book1), userRepository.getByIndex(1));
        orderService.createOrder(Arrays.asList(book2, book3), userRepository.getByIndex(1));
        orderService.createOrder(Arrays.asList(book2), userRepository.getByIndex(1));
        orderService.createOrder(Arrays.asList(book3), userRepository.getByIndex(1));

        orderService.completeOrder(0);
        orderService.completeOrder(1);
        orderService.completeOrder(2);
        orderService.completeOrder(3);

        OrderController oc = OrderController.getInstance();
        oc.getCompletedOrdersForPeriod(OrderComparator.DateComparatorDescending, "2021-08-05", "2021-08-11");

        MenuController menuController = MenuController.getInstance();
        menuController.run();

    }
}
