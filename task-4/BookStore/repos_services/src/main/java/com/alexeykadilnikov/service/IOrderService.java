package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.OrderStatus;
import com.alexeykadilnikov.entity.User;

import java.util.Date;

public interface IOrderService {
    void createOrder(Book[] books, User user, Date date);
    String showOrder();
    void cancelOrder();
    void setStatus(int index, OrderStatus status);
    void completeOrder(int id);
}
