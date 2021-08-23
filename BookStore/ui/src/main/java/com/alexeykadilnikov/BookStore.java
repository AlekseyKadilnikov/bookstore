package com.alexeykadilnikov;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.controller.ControllerUtils;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.view.menu.MenuController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BookStore {
    @InjectBean
    private ControllerUtils controllers;
    @InjectBean
    private MenuController menuController;

    private static final Logger logger = LoggerFactory.getLogger(BookStore.class);

    void start() {
        loadState();
        menuController.run();
    }

    void loadState() {
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
            ControllerUtils.bookController.saveAll(bookList);
            ControllerUtils.orderController.saveAll(orderList);
            ControllerUtils.userController.saveAll(userList);
            ControllerUtils.requestController.saveAll(requestList);
            fis.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error with reading a file!");
        }
    }
}
