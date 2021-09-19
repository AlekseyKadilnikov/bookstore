package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.OrderDto;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.OrderBook;
import com.alexeykadilnikov.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public interface IOrderService {
    OrderDto setStatus(long index, int statusCode);
    List<OrderDto> getAll();
    OrderDto save(OrderDto orderDto);
    OrderDto getById(long id);
    int calculatePrice(Order order);
    List<Order> sort(List<Order> orders, Comparator<Order> comparator);
    List<Order> sendSqlQuery(String hql);
    List<OrderDto> sortBy(String sortBy, int mode);
    List<OrderDto> sortByStatus(int statusCode);
    List<OrderDto> sortForPeriod(String sortBy, int mode, String startDate, String endDate);
    int getEarnedMoneyForPeriod(String startDate, String endDate);
    int getCountOfCompleteOrdersForPeriod(String startDate, String endDate);
}
