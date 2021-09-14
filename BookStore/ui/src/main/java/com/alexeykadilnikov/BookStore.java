package com.alexeykadilnikov;

import com.alexeykadilnikov.controller.ControllerUtils;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.view.menu.MenuController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookStore {
    private final MenuController menuController;

    @Autowired
    public BookStore(MenuController menuController) {
        this.menuController = menuController;
    }

    private static final Logger logger = LoggerFactory.getLogger(BookStore.class);

    void start() {
//        loadState();
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
            ControllerUtils.getBookController().saveAll(bookList);
            ControllerUtils.getOrderController().saveAll(orderList);
            ControllerUtils.getUserController().saveAll(userList);
            ControllerUtils.getRequestController().saveAll(requestList);
            fis.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error with reading a file!");
        }
    }
}
