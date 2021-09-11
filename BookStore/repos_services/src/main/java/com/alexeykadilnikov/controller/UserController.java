package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
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
        System.out.println(user.getOrders());
    }

    public List<User> getAll() {
        return userService.getAll();
    }

    public void saveAll(List<User> userList) {
        userService.saveAll(userList);
    }

    public void importUsers(String path) {}

    public void exportUsers(String path, String userIds) {}
}
