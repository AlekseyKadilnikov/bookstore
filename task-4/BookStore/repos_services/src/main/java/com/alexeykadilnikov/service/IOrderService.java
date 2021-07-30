package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.OrderStatus;
import com.alexeykadilnikov.entity.User;

public interface IOrderService {
    void createOrder(Book[] books, User user);
    String showOrder();
    void cancelOrder();
    void setStatus(OrderStatus status);
    void completeOrder(int id);
}
