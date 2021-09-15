package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.dao.IUserDAO;
import com.alexeykadilnikov.dto.OrderDto;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.dao.IBookDAO;
import com.alexeykadilnikov.dao.IOrderDAO;
import com.alexeykadilnikov.dao.IRequestDAO;
import com.alexeykadilnikov.mapper.OrderMapper;
import com.alexeykadilnikov.utils.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService implements IOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final IOrderDAO orderDAO;
    private final IBookDAO bookDAO;
    private final IRequestDAO requestDAO;
    private final IUserDAO userDAO;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(IOrderDAO orderDAO, IBookDAO bookDAO, IRequestDAO requestDAO, IUserDAO userDAO, OrderMapper orderMapper) {
        this.orderDAO = orderDAO;
        this.bookDAO = bookDAO;
        this.requestDAO = requestDAO;
        this.userDAO = userDAO;
        this.orderMapper = orderMapper;
    }

    private OrderDto create(List<Long> booksId, User user) {
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
        return orderMapper.toDto(order);
    }

    private OrderDto cancel(long id) {
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
        return orderMapper.toDto(order);
    }

    public OrderDto setStatus(long id, int statusCode) {
        if(statusCode == 1) {
            return complete(id);
        } else if(statusCode == 2) {
            return cancel(id);
        } else {
            Order order = orderDAO.getById(id);
            order.setStatus(OrderStatus.NEW);
            return orderMapper.toDto(orderDAO.update(order));
        }
    }

    private OrderDto complete(long id) {
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
                return orderMapper.toDto(order);
            }
        }
        order.setStatus(OrderStatus.SUCCESS);
        order.setExecutionDate(LocalDateTime.now());
        return orderMapper.toDto(order);
    }

    public List<OrderDto> getAll() {
        List<Order> orders = orderDAO.findAll();
        List<OrderDto> ordersDto = new ArrayList<>();
        for(Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            ordersDto.add(orderDto);
        }
        return ordersDto;
    }

    public OrderDto save(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        if(order.getStatus() == OrderStatus.NEW) {
            checkBookAvailable(order.getOrderBooks());
        }
        order.setTotalPrice(calculatePrice(order));
        order = orderDAO.save(order);
        return orderMapper.toDto(order);
    }

    private void checkBookAvailable(Set<OrderBook> orderBooks) {
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

    public OrderDto getById(long id) {
        return orderMapper.toDto(orderDAO.getById(id));
    }

    public int calculatePrice(Order order) {
        int totalPrice = 0;
        for(OrderBook orderBook : order.getOrderBooks()) {
            Book book = orderBook.getBook();
            totalPrice += book.getPrice() * orderBook.getBookCount();
        }
        return totalPrice;
    }

    public List<Order> sort(List<Order> orders, Comparator<Order> comparator) {
        orders.sort(comparator);
        return orders;
    }

    public List<Order> sendSqlQuery(String hql) {
        return orderDAO.findAll(hql);
    }

    public List<OrderDto> sortBy(String sortBy, int mode) {
        String hql = "";
        switch (sortBy) {
            case "price":
                hql = QueryBuilder.sortOrdersByPrice(mode);
                break;
            case "execDate":
                hql = QueryBuilder.sortOrdersByExecDate(mode);
                break;
            default:
                return new ArrayList<>();
        }

        List<Order> orders = sendSqlQuery(hql);
        List<OrderDto> ordersDto = new ArrayList<>();
        for(Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            ordersDto.add(orderDto);
        }
        return ordersDto;
    }

    public List<OrderDto> sortByStatus(int statusCode) {
        List<Order> orders = sendSqlQuery(QueryBuilder.sortByStatus(OrderStatus.values()[statusCode]));
        List<OrderDto> ordersDto = new ArrayList<>();
        for(Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            ordersDto.add(orderDto);
        }
        return ordersDto;
    }

    public List<OrderDto> sortForPeriod(String sortBy, int mode, String startDate, String endDate) {
        String hql = "";
        switch (sortBy) {
            case "execDate":
                hql = QueryBuilder.sortOrdersByExecDateForPeriodByDate(startDate, endDate, mode);
                break;
            case "price":
                hql = QueryBuilder.sortOrdersByExecDateForPeriodByPrice(startDate, endDate, mode);
                break;
            default:
                return new ArrayList<>();
        }
        List<Order> orders = sendSqlQuery(hql);
        List<OrderDto> ordersDto = new ArrayList<>();
        for(Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            ordersDto.add(orderDto);
        }
        return ordersDto;
    }

    public int getEarnedMoneyForPeriod(String startDate, String endDate) {
        String hql = QueryBuilder.getCompleteOrdersForPeriod(startDate, endDate);

        List<Order> orders = sendSqlQuery(hql);

        int sum = 0;
        for(Order order : orders) {
            sum += order.getTotalPrice();
        }

        return sum;
    }

    public int getCountOfCompleteOrdersForPeriod(String startDate, String endDate) {
        String hql = QueryBuilder.getCompleteOrdersForPeriod(startDate, endDate);
        return sendSqlQuery(hql).size();
    }
}
