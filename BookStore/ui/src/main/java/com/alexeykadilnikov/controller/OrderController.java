package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.service.OrderService;

public class OrderController {
    private static OrderController instance;

    private OrderService orderService;

    private OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public static OrderController getInstance() {
        if(instance == null) {
            instance = new OrderController(OrderService.getInstance());
        }
        return instance;
    }
}
