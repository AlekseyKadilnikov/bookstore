package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.dao.IBookDAO;
import com.alexeykadilnikov.dao.IOrderDAO;
import com.alexeykadilnikov.dao.IRequestDAO;
import com.alexeykadilnikov.dao.IUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

@Singleton
public class OrderService implements IOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @InjectBean
    private IOrderDAO orderDAO;
    @InjectBean
    private IUserDAO userDAO;
    @InjectBean
    private IBookDAO bookDAO;
    @InjectBean
    private IRequestDAO requestDAO;

    @Override
    public void createOrder(List<Long> booksId, User user) {
        Set<OrderBook> orderBooks = new HashSet<>();
        Map<Long, Integer> books = new HashMap<>();
        for(Long bookId : booksId) {
            if(books.containsKey(bookId)) {
                int val = books.get(bookId) + 1;
                books.put(bookId, val);
            } else {
                books.put(bookId, 1);
            }
        }
        Order order = new Order();
        for(Map.Entry<Long, Integer> entry : books.entrySet()) {
            OrderBook orderBook = new OrderBook();
            orderBook.setOrder(order);
            orderBook.setBook(bookDAO.getById(entry.getKey()));
            orderBook.setBookCount(entry.getValue());
            orderBooks.add(orderBook);
        }
        order.setOrderBooks(orderBooks);
        order.setUser(user);
        order.setTotalPrice(calculatePrice(order));
        order.setStatus(OrderStatus.NEW);
        orderDAO.save(order);
        checkBookAvailable(orderBooks);
    }

    @Override
    public String showOrder(long id) {
        return orderDAO.getById(id).toString();
    }

    @Override
    public void cancelOrder(long id) {
        Order order = orderDAO.getById(id);
        for(OrderBook orderBook : order.getOrderBooks()) {
            Request newRequest = orderBook.getBook().getRequests()
                    .stream()
                    .filter(request -> request.getStatus() == RequestStatus.NEW)
                    .findFirst()
                    .orElse(null);
            if(newRequest != null) {
                if(newRequest.getCount() <= orderBook.getBookCount()) {
                    requestDAO.delete(newRequest);
                } else {
                    newRequest.setCount(newRequest.getCount() - orderBook.getBookCount());
                }
            }
            Book book = orderBook.getBook();
            book.setCount(book.getCount() + orderBook.getBookCount());
            bookDAO.update(book);
        }
        order.setStatus(OrderStatus.CANCELED);
        orderDAO.update(order);
        logger.info("Order id = {} canceled", order.getId());
    }

    @Override
    public void setStatus(long id, OrderStatus status) {
        if(status == OrderStatus.SUCCESS) {
            completeOrder(id);
            return;
        }
        Order order = orderDAO.getById(id);
        order.setStatus(status);
        orderDAO.update(order);
    }

    @Override
    public void completeOrder(long id) {
        Order order = orderDAO.getById(id);
        for(OrderBook orderBook : order.getOrderBooks()) {
            Book book = orderBook.getBook();
            Request newRequest = book.getRequests()
                    .stream()
                    .filter(request -> request.getStatus() == RequestStatus.NEW)
                    .findFirst()
                    .orElse(null);
            if(newRequest == null) continue;
            if(newRequest.getStatus() == RequestStatus.NEW && newRequest.getCount() > 0) {
                logger.info("Order id = {} couldn't be completed: request for book id = {} not closed",
                        order.getId(),
                        book.getId());
                return;
            }
        }
        order.setStatus(OrderStatus.SUCCESS);
        order.setExecutionDate(LocalDateTime.now());
        orderDAO.update(order);
    }

    @Override
    public List<Order> getAll() {
        return orderDAO.findAll();
    }

    @Override
    public void saveOrder(Order order) {
        if(order.getStatus() == OrderStatus.NEW) {
            checkBookAvailable(order.getOrderBooks());
        }
        order.setTotalPrice(calculatePrice(order));
        orderDAO.save(order);
    }

    @Override
    public void saveAll(List<Order> orderList) {
        for(Order order : orderList) {
            orderDAO.save(order);
        }
    }

    @Override
    public void checkBookAvailable(Set<OrderBook> orderBooks) {
        for (OrderBook orderBook : orderBooks) {
            Book book = orderBook.getBook();
            int diff = orderBook.getBookCount() - book.getCount();
            if(diff > 0) {
                Request newRequest = book.getRequests()
                        .stream()
                        .filter(request -> request.getStatus() == RequestStatus.NEW)
                        .findFirst()
                        .orElse(null);
                String name = "Request for book with id = " + book.getId();
                if(newRequest == null) {
                    newRequest = new Request();
                    newRequest.setName(name);
                    newRequest.setStatus(RequestStatus.NEW);
                    newRequest.setBooks(Collections.singleton(book));
                    newRequest.setCount(newRequest.getCount() + diff);
                    requestDAO.save(newRequest);
                }
                else {
                    newRequest.setCount(newRequest.getCount() + diff);
                    newRequest.getBooks().add(book);
                    requestDAO.update(newRequest);
                }
                Request commonRequest = book.getRequests()
                        .stream()
                        .filter(request -> request.getStatus() == RequestStatus.COMMON)
                        .filter(request -> request.getName().equals(name))
                        .findFirst()
                        .orElse(null);
                if(commonRequest == null) {
                    commonRequest = new Request();
                    commonRequest.setName("Request for book with id = " + book.getId());
                    commonRequest.setStatus(RequestStatus.COMMON);
                    commonRequest.setBooks(Collections.singleton(book));
                    commonRequest.setCount(commonRequest.getCount() + diff);
                    requestDAO.save(commonRequest);
                } else {
                    newRequest.setCount(newRequest.getCount() + diff);
                    newRequest.getBooks().add(book);
                    requestDAO.update(commonRequest);
                }
                book.setCount(0);
            }
            else {
                book.setCount(book.getCount() - orderBook.getBookCount());
            }
            bookDAO.update(book);
        }
    }

    @Override
    public Order getByIndex(long id) {
        return orderDAO.getById(id);
    }

    @Override
    public Order getById(long id) {
        return orderDAO.getById(id);
    }

    @Override
    public int calculatePrice(Order order) {
        int totalPrice = 0;
        for(OrderBook orderBook : order.getOrderBooks()) {
            Book book = orderBook.getBook();
            totalPrice += book.getPrice() * orderBook.getBookCount();
        }
        return totalPrice;
    }

    @Override
    public List<Order> sort(List<Order> orders, Comparator<Order> comparator) {
        orders.sort(comparator);
        return orders;
    }

    @Override
    public List<Order> sendSqlQuery(String hql) {
        return orderDAO.findAll(hql);
    }
}
