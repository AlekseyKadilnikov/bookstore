package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.utils.UserUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderController {
    private static OrderController instance;

    private final OrderService orderService;

    private OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public static OrderController getInstance() {
        if(instance == null) {
            instance = new OrderController(OrderService.getInstance());
        }
        return instance;
    }

    public void create(List<Integer> ids) {
        BookService bookService = BookService.getInstance();
        List<Book> booksAtId = new ArrayList<>();
        for(int id : ids) {
            for(Book book : bookService.getAll()) {
                if(book.getId() == id) {
                    booksAtId.add(book);
                }
            }
        }

        orderService.createOrder(booksAtId, UserUtils.getCurrentUser());
    }

    public void cancel(int id) {
        orderService.cancelOrder(id);
    }

    public List<Order> getAll() {
        return orderService.getAll();
    }

    public void showOne(int id) {
        System.out.println(orderService.showOrder(id));
    }

    public void showAll() {
        List<Order> orders = orderService.getAll();
        for(Order order : orders) {
            System.out.println(order.toString());
        }
    }

    public void sort(List<Order> sortedOrders, Comparator<Order> comparator) {
        sortedOrders = orderService.sort(sortedOrders, comparator);
        System.out.println(sortedOrders.toString());
    }

    public List<Order> sortByStatus(OrderStatus status) {
        List<Order> sortedOrders = new ArrayList<>();
        for(Order order : orderService.getAll()) {
            if(order.getStatus() == status) {
                sortedOrders.add(order);
            }
        }

        return sortedOrders;
    }

    public void getCompletedOrdersForPeriod(Comparator<Order> comparator, String dateAfterS, String dateBeforeS) {
        LocalDate dateAfter = LocalDate.parse(dateAfterS);
        LocalDate dateBefore = LocalDate.parse(dateBeforeS);

        List<Order> orders = sortByStatus(OrderStatus.SUCCESS);
        List<Order> ordersBetweenDates = new ArrayList<>();

        for(Order order : orders) {
            if(order.getExecutionDate().isAfter(dateAfter) && order.getExecutionDate().isBefore(dateBefore)) {
                ordersBetweenDates.add(order);
            }
        }

        sort(ordersBetweenDates, comparator);
    }

    public void showEarnedMoneyForPeriod(String dateAfterS, String dateBeforeS) {
        int sum = 0;
        LocalDate dateAfter = LocalDate.parse(dateAfterS);
        LocalDate dateBefore = LocalDate.parse(dateBeforeS);
        List<Order> orders = sortByStatus(OrderStatus.SUCCESS);
        for(Order order : orders) {
            if(order.getExecutionDate().isAfter(dateAfter) && order.getExecutionDate().isBefore(dateBefore)) {
                sum += order.getTotalPrice();
            }
        }
        System.out.println("Sum = " + sum);
    }

    public void showCompletedOrdersCountForPeriod(String dateAfterS, String dateBeforeS) {
        int count = 0;
        LocalDate dateAfter = LocalDate.parse(dateAfterS);
        LocalDate dateBefore = LocalDate.parse(dateBeforeS);
        List<Order> orders = sortByStatus(OrderStatus.SUCCESS);
        for(Order order : orders) {
            if(order.getExecutionDate().isAfter(dateAfter) && order.getExecutionDate().isBefore(dateBefore)) {
                count++;
            }
        }
        System.out.println("Count = " + count);
    }
}
