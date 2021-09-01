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

import java.time.LocalDate;
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
        List<Book> books = new ArrayList<>();
        for(Long id : booksId) {
            books.add(bookRepository.getById(id));
        }
        Order order = new Order(books, user.getId());
        checkBookAvailable(books, order.getId());
        order.setTotalPrice(calculatePrice(order));
        orderRepository.save(order);
        user.addOrder(order.getId());
    }

    @Override
    public String showOrder(long id) {
        return orderRepository.getById(id).toString();
    }

    @Override
    public void cancelOrder(long id) {
        Order order = orderRepository.getById(id);
        for(Book book : order.getBooks()) {
            book.setCount(book.getCount() + 1);
            Request[] orderRequests = book.getOrderRequests();
            for(Request request : orderRequests) {
                if(request.getStatus() == RequestStatus.NEW) {
                    bookRepository.addRequest(new Request(request.getName(),
                            book.getId(), request.getOrdersId(), RequestStatus.SUCCESS), 1, book);
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
        orderRepository.getById(id).setStatus(status);
    }

    @Override
    public void completeOrder(long id) {
        Order order = orderRepository.getById(id);
        for(Book book : order.getBooks()) {
            for(Request request : book.getOrderRequests()) {
                if(request.getStatus() == RequestStatus.NEW) {
                    logger.info("Order id = {} couldn't be completed: request for book id = {} not closed",
                            order.getId(),
                            book.getId());
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
    public void checkBookAvailable(List<Book> books, long orderId) {
        for (Book book : books) {
            if(book.getCount() == 0) {
                Request[] orderRequests = book.getOrderRequests();
                if (orderRequests[0] == null) {
                    orderRequests[0] = new Request();
                    orderRequests[0].setName("Request for book with id = " + book.getId());
                    orderRequests[0].setStatus(RequestStatus.NEW);
                }
                if (orderRequests[1] == null) {
                    orderRequests[1] = new Request();
                    orderRequests[1].setName("Request for book with id = " + book.getId());
                    orderRequests[1].setStatus(RequestStatus.SUCCESS);
                }
                orderRequests[0].setCount(orderRequests[0].getCount() + 1);
                orderRequests[0].setOrdersId(Collections.singleton(orderId));
                requestRepository.save(orderRequests[0]);

                Request request = new Request("Request for book with id = " + book.getId(),
                        Collections.singleton(book.getId()));
                bookRepository.addRequest(request, 1, book);
                requestRepository.save(request);
            }
            else {
                book.setCount(book.getCount() - 1);
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
        for (Book book : order.getBooks()) {
            totalPrice += book.getPrice();
        }
        return totalPrice;
    }

    @Override
    public List<Order> sort(List<Order> orders, Comparator<Order> comparator) {
        orders.sort(comparator);
        return orders;
    }
}
