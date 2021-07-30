package com.alexeykadilnikov.service;

import com.alexeykadilnikov.comparator.OrderComparator;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.RequestRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final RequestRepository requestRepository;

    public OrderService(OrderRepository orderRepository, RequestRepository requestRepository) {
        this.orderRepository = orderRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void createOrder(Book[] books, User user, Date date) {
        for (Book book : books) {
            if(!book.isAvailable()) {
                Request request = new Request(book, user);
                requestRepository.save(request);
                System.out.println("Request id = " + request.getId() + ", date = " + request.getDate() + " created");
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
    public void setStatus(int index, OrderStatus status) {
        orderRepository.getByIndex(index).setStatus(status);
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
        order.setStatus(OrderStatus.COMPLETED);
        order.setExecutionDate(new Date());
        System.out.println("Order id = " + order.getId() + " completed");
    }

    @Override
    public Order[] getAll() {
        return orderRepository.findAll();
    }

    public Order[] getOrderListForPeriod(Date dateAfter, Date dateBefore) {
        Order[] orders = new Order[0];
        for(Order order : orderRepository.findAll()){
                if(order.getStatus() == OrderStatus.COMPLETED &&
                        order.getExecutionDate().after(dateAfter) &&
                        order.getExecutionDate().before(dateBefore)) {
                    Order[] newOrder = new Order[orders.length + 1];
                    System.arraycopy(orders, 0, newOrder, 0, orders.length);
                    newOrder[orders.length] = order;
                    orders = newOrder;
            }
        }
        return orders;
    }

    public Order[] sortByExecutionDateAscending(Order[] orders) {
        Arrays.sort(orders, OrderComparator.DateComparatorAscending);
        return orders;
    }

    public Order[] sortByExecutionDateDescending(Order[] orders) {
        Arrays.sort(orders, OrderComparator.DateComparatorDescending);
        return orders;
    }

    public Order[] sortByPriceAscending(Order[] orders) {
        Arrays.sort(orders, OrderComparator.PriceComparatorAscending);
        return orders;
    }

    public Order[] sortByPriceDescending(Order[] orders) {
        Arrays.sort(orders, OrderComparator.PriceComparatorDescending);
        return orders;
    }

    public Order[] sortByStatusAscending(Order[] orders) {
        Arrays.sort(orders, OrderComparator.StatusComparatorAscending);
        return orders;
    }

    public Order[] sortByStatusDescending(Order[] orders) {
        Arrays.sort(orders, OrderComparator.StatusComparatorDescending);
        return orders;
    }
}
