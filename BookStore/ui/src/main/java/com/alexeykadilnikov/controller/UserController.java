package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.service.UserService;

public class UserController {
    private static UserController instance;

    private UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    public int create(String username) {
        if(userService.addUser(username) > 0) {
            System.out.println("Error: user with the same name already exists. Please try again");
            return 1;
        }
        return 0;
    }

    public int getOne(String username) {
        if(userService.getByName(username) == null) {
            System.out.println("User with this name does not exist. Please try again");
            return 1;
        }
        return 0;
    }

    public static UserController getInstance() {
        if(instance == null) {
            instance = new UserController(UserService.getInstance());
        }
        return instance;
    }
}
