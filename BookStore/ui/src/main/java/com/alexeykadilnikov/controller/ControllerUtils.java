package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.annotation.InjectBean;

public class ControllerUtils {
    @InjectBean
    public static BookController bookController;
    @InjectBean
    public static OrderController orderController;
    @InjectBean
    public static UserController userController;
    @InjectBean
    public static RequestController requestController;
}
