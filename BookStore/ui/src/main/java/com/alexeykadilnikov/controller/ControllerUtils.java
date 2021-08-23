package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;

@Singleton
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
