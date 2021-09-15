package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.dto.UserDto;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.mapper.UserMapper;
import com.alexeykadilnikov.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(IUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public UserDto create(UserDto userDto) {
        return userService.save(userDto);
    }

    public UserDto getOne(String username) {
        return userService.getByName(username);
    }

    public void getOrders(User user) {
        System.out.println(user.getOrders());
    }

    public List<UserDto> getAll() {
        return userService.getAll();
    }

    public void importUsers(String path) {}

    public void exportUsers(String path, String userIds) {}
}
