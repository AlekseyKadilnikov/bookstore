package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

public class OrderService implements IOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private static OrderService instance;

    private final OrderRepository orderRepository;

    private OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void createOrder(List<Book> books, User user) {
        Order order = new Order(books, user);
        checkBookAvailable(books, order.getId());
        order.setTotalPrice(calculatePrice(order));
        orderRepository.save(order);
        user.addOrder(order);

        logger.info("Order id = {} created", order.getId());
    }

    @Override
    public String showOrder(int index) {
        return orderRepository.getByIndex(index).toString();
    }

    @Override
    public void cancelOrder(int id) {
        BookRepository bookRepository = BookRepository.getInstance();
        Order order = orderRepository.getByIndex(id);
        for(Book book : order.getBooks()) {
            book.setCount(book.getCount() + 1);
            Request[] orderRequests = book.getOrderRequests();
            for(Request request : orderRequests) {
                if(request.getStatus() == RequestStatus.NEW) {
                    bookRepository.addRequest(new Request(request.getName(),
                            book.getId(), request.getOrdersId(), RequestStatus.SUCCESS), 1, book.getId());
                    if(request.getCount() > 0) {
                        request.setCount(request.getCount() - 1);
                    }
                    else {
                        orderRequests[0].setCount(0);
                    }
                }
            }
        }
        order.setStatus(OrderStatus.CANCELED);
        logger.info("Order id = {} canceled", order.getId());
    }

    @Override
    public void setStatus(int index, OrderStatus status) {
        orderRepository.getByIndex(index).setStatus(status);
    }

    @Override
    public void completeOrder(int id) {
        Order order = orderRepository.getByIndex(id);
        for(Book book : order.getBooks()) {
            for(Request request : book.getOrderRequests()) {
                if(request.getStatus() == RequestStatus.NEW) {
                    logger.info("Order id = {} couldn't be completed: request for {} - {} not closed", order.getId(),
                            book.getAuthor(), book.getName());
                    return;
                }
            }
        }
        order.setStatus(OrderStatus.SUCCESS);
        Random random = new Random();
        LocalDate date = LocalDate.now();
        date = date.plusDays(random.nextInt(4));
        order.setExecutionDate(date);
        logger.info("Order id = {} completed", order.getId());
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public static OrderService getInstance() {
        if(instance == null) {
            instance = new OrderService(OrderRepository.getInstance());
        }
        return instance;
    }

    public void saveOrder(Order order) {
        if(order.getStatus() == OrderStatus.NEW) {
            checkBookAvailable(order.getBooks(), order.getId());
        }
        order.setTotalPrice(calculatePrice(order));
        order.getUser().addOrder(order);
        orderRepository.save(order);
    }

    private void checkBookAvailable(List<Book> books, long orderId) {
        for (Book book : books) {
            if(book.getCount() == 0) {
                BookRepository bookRepository = BookRepository.getInstance();
                Request[] orderRequests = book.getOrderRequests();
                orderRequests[0].setCount(orderRequests[0].getCount() + 1);
                orderRequests[0].setOrdersId(Collections.singleton(orderId));
                logger.info("Order request for {} - {} created", book.getAuthor(), book.getName());

                bookRepository.addRequest(new Request("Request for " + book.getAuthor() + " - " + book.getName(),
                        Collections.singleton(book.getId())), 1, book.getId());
                logger.info("Common request for {} - {} created", book.getAuthor(), book.getName());
            }
            else {
                book.setCount(book.getCount() - 1);
            }
        }
    }

    public Order getByIndex(int index) {
        return orderRepository.getByIndex(index);
    }

    public Order getById(long id) {
        return orderRepository.getById(id);
    }

    public int calculatePrice(Order order) {
        int totalPrice = 0;
        for (Book book : order.getBooks()) {
            totalPrice += book.getPrice();
        }
        return totalPrice;
    }

    public List<Order> sort(List<Order> orders, Comparator<Order> comparator) {
        orders.sort(comparator);
        return orders;
    }
}