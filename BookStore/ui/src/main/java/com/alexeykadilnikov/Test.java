package com.alexeykadilnikov;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.RequestRepository;
import com.alexeykadilnikov.repository.UserRepository;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.service.RequestService;
import com.alexeykadilnikov.view.menu.MenuController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private static final OrderService orderService = OrderService.getInstance();
    private static final BookService bookService = BookService.getInstance();
    private static final RequestService requestService = RequestService.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();
    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final OrderRepository orderRepository = OrderRepository.getInstance();
    private static final RequestRepository requestRepository = RequestRepository.getInstance();

    public static void main(String[] args) {
        loadState();

//        LocalDate localDate = LocalDate.now();
//        Book book = bookRepository.getById(0L);
//        Book book1 = bookRepository.getById(1L);
//        Book book2 = bookRepository.getById(2L);
//        Book book3 = bookRepository.getById(3L);
//
//        book.setDateOfReceipt(localDate.minusMonths(1));
//        book1.setDateOfReceipt(localDate.minusMonths(2));
//        book2.setDateOfReceipt(localDate.minusMonths(3));
//        book3.setDateOfReceipt(localDate.minusMonths(4));
//
//        orderService.createOrder(Arrays.asList(book, book1), userRepository.getByIndex(1));
//        orderService.createOrder(Arrays.asList(book2, book3), userRepository.getByIndex(1));
//        orderService.createOrder(Arrays.asList(book2, book2, book2), userRepository.getByIndex(1));
//        orderService.createOrder(Arrays.asList(book2, book3), userRepository.getByIndex(1));
//        bookService.addBook(2, 2);
//        orderService.completeOrder(0);
//        orderService.completeOrder(1);
//        orderService.completeOrder(2);
//        orderService.completeOrder(3);
//
//        System.out.println(orderService.getById(0).toString());
//
//        requestService.createRequest("читать Бесы достоевский", 1);
//        requestService.createRequest("читать Бесы достоевский", 1);
//        requestService.createRequest("Бесы Федор", 1);
//        requestService.createRequest("достоевский Бесы", 1);
//        requestService.createRequest("достоевский Бесы", 1);
//        requestService.createRequest("достоевский Бесы", 1);
//        requestService.createRequest("федор достоевский", 1);
//        requestService.createRequest("достоевский", 200);
//
//        bookRepository.getById(0L).setDescription("Description1");
//        bookRepository.getById(1L).setDescription("Description2");
//        bookRepository.getById(2L).setDescription("Description3");
//        bookRepository.getById(3L).setDescription("Description4");
//        bookRepository.getById(4L).setDescription("Description5");
//        bookRepository.getById(5L).setDescription("Description6");
//        bookRepository.getById(6L).setDescription("Description7");

        MenuController menuController = MenuController.getInstance();
        menuController.run();
    }

    private static void loadState() {
        List<Book> bookList;
        List<Order> orderList;
        List<User> userList;
        List<Request> requestList;

        try {
            FileInputStream fis = new FileInputStream("serialize\\books.bin");
            ObjectInputStream in = new ObjectInputStream(fis);
            bookList = (ArrayList) in.readObject();
            fis = new FileInputStream("serialize\\orders.bin");
            in = new ObjectInputStream(fis);
            orderList = (ArrayList) in.readObject();
            fis = new FileInputStream("serialize\\users.bin");
            in = new ObjectInputStream(fis);
            userList = (ArrayList) in.readObject();
            fis = new FileInputStream("serialize\\requests.bin");
            in = new ObjectInputStream(fis);
            requestList = (ArrayList) in.readObject();
            bookRepository.saveAll(bookList);
            orderRepository.saveAll(orderList);
            userRepository.saveAll(userList);
            requestRepository.saveAll(requestList);
            fis.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error with reading a file!");
        }
    }
}
