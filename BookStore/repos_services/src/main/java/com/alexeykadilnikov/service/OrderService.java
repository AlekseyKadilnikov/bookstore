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
        Map<Long, Integer> books = new HashMap<>();
        for(Long bookId : booksId) {
            if(books.containsKey(bookId)) {
                int val = books.get(bookId) + 1;
                books.put(bookId, val);
            } else {
                books.put(bookId, 1);
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
        for(Map.Entry<Long, Integer> entry : order.getBooks().entrySet()) {
            Book book = bookRepository.getById(entry.getKey());
            book.setCount(book.getCount() + 1);
            Request[] orderRequests = book.getOrderRequests();
            for(Request request : orderRequests) {
                if(request.getStatus() == RequestStatus.NEW) {
                    Set<Long> booksId = new HashSet<>();
                    booksId.add(book.getId());
                    bookRepository.addRequest(new Request(request.getName(),
                            booksId, request.getOrdersId(), RequestStatus.SUCCESS), 1, book);
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
        for(Map.Entry<Long, Integer> entry : order.getBooks().entrySet()) {
            Book book = bookRepository.getById(entry.getKey());
            for(Request request : book.getOrderRequests()) {
                if(request == null) continue;
                if(request.getStatus() == RequestStatus.NEW && request.getCount() > 0) {
                    logger.info("Order id = {} couldn't be completed: request for book id = {} not closed",
                            order.getId(),
                            book.getId());
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
    public void checkBookAvailable(Map<Long, Integer> books, long orderId) {
        for (Map.Entry<Long, Integer> entry : books.entrySet()) {
            Book book = bookRepository.getById(entry.getKey());
            int diff = entry.getValue() - book.getCount();
            if(diff > 0) {
                Request[] orderRequests = book.getOrderRequests();
                if (orderRequests[0] == null) {
                    orderRequests[0] = new Request();
                    orderRequests[0].setName("Request for book with id = " + book.getId());
                    orderRequests[0].setStatus(RequestStatus.NEW);
                }
                orderRequests[0].setCount(diff);
                orderRequests[0].setOrdersId(Collections.singleton(orderId));
                orderRequests[0].setBooksId(Collections.singleton(entry.getKey()));
                requestRepository.save(orderRequests[0]);

                Request request = new Request("Request for book with id = " + book.getId(),
                        Collections.singleton(entry.getKey()));
                request.setCount(diff);
                requestRepository.save(request);
                bookRepository.addRequest(request, 1, book);
                book.setCount(0);
                bookRepository.update(book);
            }
            else {
                book.setCount(book.getCount() - entry.getValue());
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
        for (Map.Entry<Long, Integer> entry : order.getBooks().entrySet()) {
            Book book = bookRepository.getById(entry.getKey());
            totalPrice += book.getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    @Override
    public List<Order> sort(List<Order> orders, Comparator<Order> comparator) {
        orders.sort(comparator);
        return orders;
    }
}
