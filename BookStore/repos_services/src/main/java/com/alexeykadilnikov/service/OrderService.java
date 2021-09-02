package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.repository.IBookRepository;
import com.alexeykadilnikov.repository.IOrderRepository;
import com.alexeykadilnikov.repository.IRequestRepository;
import com.alexeykadilnikov.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

@Singleton
public class OrderService implements IOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @InjectBean
    private IOrderRepository orderRepository;
    @InjectBean
    private IUserRepository userRepository;
    @InjectBean
    private IBookRepository bookRepository;
    @InjectBean
    private IRequestRepository requestRepository;

    @Override
    public void createOrder(List<Long> booksId, User user) {
        Map<Book, Integer> books = new HashMap<>();
        for(Long id : booksId) {
            Book book = bookRepository.getById(id);
            if(books.containsKey(book)) {
                int val = books.get(book) + 1;
                books.put(book, val);
            } else {
                books.put(book, 1);
            }
        }
        Order order = new Order(books, user.getId());
        order.setTotalPrice(calculatePrice(order));
        orderRepository.save(order);
        checkBookAvailable(books, order.getId());
        user.addOrder(order.getId());
    }

    @Override
    public String showOrder(long id) {
        return orderRepository.getById(id).toString();
    }

    @Override
    public void cancelOrder(long id) {
        Order order = orderRepository.getById(id);
        for(Map.Entry<Book, Integer> entry : order.getBooks().entrySet()) {
            entry.getKey().setCount(entry.getKey().getCount() + 1);
            Request[] orderRequests = entry.getKey().getOrderRequests();
            for(Request request : orderRequests) {
                if(request.getStatus() == RequestStatus.NEW) {
                    Set<Long> booksId = new HashSet<>();
                    booksId.add(entry.getKey().getId());
                    bookRepository.addRequest(new Request(request.getName(),
                            booksId, request.getOrdersId(), RequestStatus.SUCCESS), 1, entry.getKey());
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
    public void setStatus(long id, OrderStatus status) {
        if(status == OrderStatus.SUCCESS) {
            completeOrder(id);
            return;
        }
        Order order = orderRepository.getById(id);
        order.setStatus(status);
        orderRepository.update(order);
    }

    @Override
    public void completeOrder(long id) {
        Order order = orderRepository.getById(id);
        for(Map.Entry<Book, Integer> entry : order.getBooks().entrySet()) {
            for(Request request : entry.getKey().getOrderRequests()) {
                if(request == null) continue;
                if(request.getStatus() == RequestStatus.NEW && request.getCount() > 0) {
                    logger.info("Order id = {} couldn't be completed: request for book id = {} not closed",
                            order.getId(),
                            entry.getKey().getId());
                    return;
                }
            }
        }
        order.setStatus(OrderStatus.SUCCESS);
        order.setExecutionDate(LocalDateTime.now());
        orderRepository.update(order);
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public void saveOrder(Order order) {
        if(order.getStatus() == OrderStatus.NEW) {
            checkBookAvailable(order.getBooks(), order.getId());
        }
        order.setTotalPrice(calculatePrice(order));
        User user = userRepository.getById(order.getUserId());
        user.addOrder(order.getId());
        orderRepository.save(order);
    }

    @Override
    public void saveAll(List<Order> orderList) {
        orderRepository.saveAll(orderList);
    }

    @Override
    public void checkBookAvailable(Map<Book, Integer> books, long orderId) {
        for (Map.Entry<Book, Integer> entry : books.entrySet()) {
            if(entry.getKey().getCount() == 0) {
                Request[] orderRequests = entry.getKey().getOrderRequests();
                if (orderRequests[0] == null) {
                    orderRequests[0] = new Request();
                    orderRequests[0].setName("Request for book with id = " + entry.getKey().getId());
                    orderRequests[0].setStatus(RequestStatus.NEW);
                }
                orderRequests[0].setCount(entry.getValue());
                orderRequests[0].setOrdersId(Collections.singleton(orderId));
                requestRepository.save(orderRequests[0]);

                Request request = new Request("Request for book with id = " + entry.getKey().getId(),
                        Collections.singleton(entry.getKey().getId()));
                request.setCount(entry.getValue());
                requestRepository.save(request);
                bookRepository.addRequest(request, 1, entry.getKey());
            }
            else {
                entry.getKey().setCount(entry.getKey().getCount() - 1);
            }
        }
    }

    @Override
    public Order getByIndex(long id) {
        return orderRepository.getById(id);
    }

    @Override
    public Order getById(long id) {
        return orderRepository.getById(id);
    }

    @Override
    public int calculatePrice(Order order) {
        int totalPrice = 0;
        for (Map.Entry<Book, Integer> entry : order.getBooks().entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    @Override
    public List<Order> sort(List<Order> orders, Comparator<Order> comparator) {
        orders.sort(comparator);
        return orders;
    }
}
