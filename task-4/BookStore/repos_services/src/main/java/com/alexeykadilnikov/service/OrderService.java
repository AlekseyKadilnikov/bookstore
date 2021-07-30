package com.alexeykadilnikov.service;

import com.alexeykadilnikov.comparator.OrderComparator;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.RequestRepository;

import java.util.Arrays;
import java.util.Date;

public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final RequestRepository requestRepository;

    public OrderService(OrderRepository orderRepository, RequestRepository requestRepository) {
        this.orderRepository = orderRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void createOrder(Book[] books, User user) {
        for (Book book : books) {
            if(!book.isAvailable()) {
                requestRepository.save(new Request(book, user));
                System.out.println("Request id = " + requestRepository.getRequest().getId() + " created");
            }
        }
        Order order = new Order(books, user);
        orderRepository.save(order);
        System.out.println("Order id = " + order.getId() + " created");
    }

    @Override
    public String showOrder() {
        return null;
    }

    @Override
    public void cancelOrder() {
//        orderRepository.getOrder().setStatus(OrderStatus.Canceled);
//        if(requestRepository.getRequest() != null)
//            if(requestRepository.getRequest().getBook() == orderRepository.getOrder().getBook()) {
//                requestRepository.getRequest().setStatus(RequestStatus.Closed);
//            }
//        System.out.println("Order id = " + orderRepository.getOrder().getId() + " canceled");
    }

    @Override
    public void setStatus(OrderStatus status) {
//        orderRepository.getOrder().setStatus(status);
    }

    @Override
    public void completeOrder(int id) {
        Request[] requests = requestRepository.findAll();
        Order order = orderRepository.getByIndex(id);
        for(Request request : requests) {
            if(request.getStatus() == RequestStatus.Closed)
                continue;
            for(Book book : order.getBooks()) {
                if(request.getBook() == book && request.getUser() == order.getUser()) {
                    System.out.println("Order id = " + order.getId() + " couldn't be completed: request id = " +
                            request.getId() + " not closed");
                    return;
                }
            }
        }
        order.setStatus(OrderStatus.Completed);
        order.setExecutionDate(new Date());
        System.out.println("Order id = " + order.getId() + " completed");
    }

    public Order[] sortByExecutionDateAscending() {
        Order[] orders = orderRepository.findAll().clone();
        Arrays.sort(orders, OrderComparator.DateComparatorAscending);
        return orders;
    }

    public Order[] sortByExecutionDateDescending() {
        Order[] orders = orderRepository.findAll().clone();
        Arrays.sort(orders, OrderComparator.DateComparatorDescending);
        return orders;
    }
}
