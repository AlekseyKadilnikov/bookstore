package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.UserService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class UserController {
    private static UserController instance;

    private static final String CSV_FILE_PATH_READ = "./userRead.csv";
    private static final String CSV_FILE_PATH_WRITE = "./userWrite.csv";

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    public int create(String username) {
        if(userService.addUser(username) > 0) {
            return 1;
        }
        return 0;
    }

    public User getOne(String username) {
        return userService.getByName(username);
    }

    public void getOrders(User user) {
        for(Order order : user.getOrders()) {
            System.out.println(order.toString());
        }
    }

    public static UserController getInstance() {
        if(instance == null) {
            instance = new UserController(UserService.getInstance());
        }
        return instance;
    }

    public void importUsers() {

    }

    public void exportUsers(String ids) {

    }
}
