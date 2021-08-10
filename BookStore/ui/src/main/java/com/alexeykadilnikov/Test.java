package com.alexeykadilnikov;

import com.alexeykadilnikov.controller.BookController;
import com.alexeykadilnikov.controller.OrderController;
import com.alexeykadilnikov.controller.RequestController;
import com.alexeykadilnikov.controller.UserController;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.UserRepository;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.service.RequestService;
import com.alexeykadilnikov.service.UserService;
import com.alexeykadilnikov.view.menu.MenuController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

        RequestService requestService = RequestService.getInstance();
        requestService.createRequest("читать Бесы достоевский");
        requestService.createRequest("читать Бесы достоевский");
        requestService.createRequest("Бесы Федор");
        requestService.createRequest("достоевский Бесы");
        requestService.createRequest("достоевский Бесы");
        requestService.createRequest("достоевский Бесы");
        requestService.createRequest("федор достоевский");

        bookRepository.getByIndex(0).setDateOfReceipt(LocalDate.now().minusMonths(10));
        bookRepository.getByIndex(1).setDateOfReceipt(LocalDate.now().minusMonths(12));
        bookRepository.getByIndex(2).setDateOfReceipt(LocalDate.now().minusMonths(13));

        bookRepository.getByIndex(0).setDescription("Description1");
        bookRepository.getByIndex(1).setDescription("Description2");
        bookRepository.getByIndex(2).setDescription("Description3");
        bookRepository.getByIndex(3).setDescription("Description4");
        bookRepository.getByIndex(4).setDescription("Description5");
        bookRepository.getByIndex(5).setDescription("Description6");
        bookRepository.getByIndex(6).setDescription("Description7");

//        MenuController menuController = MenuController.getInstance();
//        menuController.run();

        OrderController orderController = OrderController.getInstance();
        orderController.importOrders();
        orderController.exportOrders("-1");

        BookController bookController = BookController.getInstance();
        bookController.importBooks();
        bookController.exportBooks("6");

        UserController userController = UserController.getInstance();
        userController.importUsers();
        userController.exportUsers("-1");

        RequestController requestController = RequestController.getInstance();
    }
}
