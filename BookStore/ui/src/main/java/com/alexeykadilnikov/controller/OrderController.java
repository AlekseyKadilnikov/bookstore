package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.service.*;
import com.alexeykadilnikov.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Singleton
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @InjectBean
    private IOrderService orderService;
    @InjectBean
    private IUserService userService;
    @InjectBean
    private IBookService bookService;

    public void create(List<Long> ids) {
        orderService.createOrder(ids, UserUtils.getCurrentUser());
    }

    public void cancel(long id) {
        orderService.cancelOrder(id);
    }

    public List<Order> getAll() {
        return orderService.getAll();
    }

    public void saveAll(List<Order> orderList) {
        orderService.saveAll(orderList);
    }

    public void showOne(int id) {
        System.out.println(orderService.showOrder(id));
    }

    public void showAll() {
        System.out.println(orderService.getAll());
    }

    public void sortByPrice(int mode) {
        orderService.sortByPrice(mode);
    }

    public void sortByExecDate(int mode) {
        orderService.sortByExecDate(mode);
    }

    public void sortByExecDateForPeriodByDate(String startDate, String endDate, int mode) {
        orderService.sortByExecDateForPeriodByDate(startDate, endDate, mode);
    }

    public void sortByExecDateForPeriodByPrice(String startDate, String endDate, int mode) {
        orderService.sortByExecDateForPeriodByPrice(startDate, endDate, mode);
    }

    public void getEarnedMoneyForPeriod(String startDate, String endDate) {
        orderService.getEarnedMoneyForPeriod(startDate, endDate);
    }

    public void getCountOfCompleteOrdersForPeriod(String startDate, String endDate) {
        orderService.getCountOfCompleteOrdersForPeriod(startDate, endDate);
    }

    public List<Order> sortByStatus(OrderStatus status) {
        return orderService.sortByStatus(status);
    }

    public void setStatus(int orderId, OrderStatus status) {
        orderService.setStatus(orderId, status);
    }


    public void importOrders(String path) {}

    public void exportOrders(String path, String orderIds) {}
}
