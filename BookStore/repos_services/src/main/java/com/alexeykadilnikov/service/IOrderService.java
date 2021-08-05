package com.alexeykadilnikov.service;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.User;

import java.util.List;

public interface IOrderService {
    void createOrder(List<Book> books, User user);
    String showOrder(int index);
    void cancelOrder(int id);
    void setStatus(int index, OrderStatus status);
    void completeOrder(int id);
    List<Order> getAll();
}
