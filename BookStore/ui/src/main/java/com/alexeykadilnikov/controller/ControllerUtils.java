package com.alexeykadilnikov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControllerUtils {
    @Autowired
    public static BookController bookController;
    @Autowired
    public static OrderController orderController;
    @Autowired
    public static UserController userController;
    @Autowired
    public static RequestController requestController;
}
