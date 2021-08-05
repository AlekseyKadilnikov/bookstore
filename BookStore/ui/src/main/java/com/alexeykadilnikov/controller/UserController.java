package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.service.UserService;

public class UserController {
    private static UserController instance;

    private UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    public static UserController getInstance() {
        if(instance == null) {
            instance = new UserController(UserService.getInstance());
        }
        return instance;
    }
}
