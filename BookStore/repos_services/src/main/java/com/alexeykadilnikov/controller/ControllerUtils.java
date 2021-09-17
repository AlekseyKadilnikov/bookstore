package com.alexeykadilnikov.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ControllerUtils {
    private static ApplicationContext context;

    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static BookController getBookController() {
        return context.getBean(BookController.class);
    }

    public static OrderController getOrderController() {
        return context.getBean(OrderController.class);
    }

    public static UserController getUserController() {
        return context.getBean(UserController.class);
    }

    public static RequestController getRequestController() {
        return context.getBean(RequestController.class);
    }
}
