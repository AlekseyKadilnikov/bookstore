package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.dto.OrderDto;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.mapper.OrderMapper;
import com.alexeykadilnikov.repository.IBookRepository;
import com.alexeykadilnikov.repository.IOrderRepository;
import com.alexeykadilnikov.repository.IRequestRepository;
import com.alexeykadilnikov.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService implements IOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final IOrderRepository orderRepository;
    private IBookRepository bookRepository;
    private IRequestRepository requestRepository;
    private IUserRepository userRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(IOrderRepository orderRepository,
                        IBookRepository bookRepository,
                        IRequestRepository requestRepository,
                        IUserRepository userRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    public OrderService(IOrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
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
            orderBook.setBook(bookRepository.getById(entry.getKey()));
            orderBook.setBookCount(entry.getValue());
            orderBooks.add(orderBook);
        }
        order.setOrderBooks(orderBooks);
        order.setUser(user);
        order.setTotalPrice(calculatePrice(order));
        order.setStatus(OrderStatus.NEW);
        orderRepository.save(order);
        checkBookAvailable(orderBooks);
        return orderMapper.toDto(order);
    }

    private OrderDto cancel(long id) {
        Order order = orderRepository.getById(id);
        for(OrderBook orderBook : order.getOrderBooks()) {
            Request newRequest = orderBook.getBook().getRequests()
                    .stream()
                    .filter(request -> request.getStatus() == RequestStatus.NEW)
                    .findFirst()
                    .orElse(null);
            if(newRequest != null) {
                if(newRequest.getCount() <= orderBook.getBookCount()) {
                    requestRepository.delete(newRequest);
                } else {
                    newRequest.setCount(newRequest.getCount() - orderBook.getBookCount());
                }
            }
            Book book = orderBook.getBook();
            book.setCount(book.getCount() + orderBook.getBookCount());
            bookRepository.save(book);
        }
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        logger.info("Order id = {} canceled", order.getId());
        return orderMapper.toDto(order);
    }

    public OrderDto setStatus(long id, int statusCode) {
        if(statusCode == 1) {
            return complete(id);
        } else if(statusCode == 2) {
            return cancel(id);
        } else {
            Order order = orderRepository.getById(id);
            order.setStatus(OrderStatus.NEW);
            return orderMapper.toDto(orderRepository.save(order));
        }
    }

    private OrderDto complete(long id) {
        Order order = orderRepository.getById(id);
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
        List<Order> orders = orderRepository.findAll();
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
        order = orderRepository.save(order);
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
                }
                else {
                    newRequest.setCount(newRequest.getCount() + diff);
                    newRequest.getBooks().add(book);
                }
                requestRepository.save(newRequest);
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
                    requestRepository.save(commonRequest);
                } else {
                    newRequest.setCount(newRequest.getCount() + diff);
                    newRequest.getBooks().add(book);
                }
                requestRepository.save(commonRequest);
                book.setCount(0);
            }
            else {
                book.setCount(book.getCount() - orderBook.getBookCount());
            }
            bookRepository.save(book);
        }
    }

    public OrderDto getById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()) {
            throw new NullPointerException("Order with id = " + id + " not found");
        }
        return orderMapper.toDto(order.get());
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

    public List<OrderDto> sortBy(String sortBy, String direction) {
        List<Order> orders;
        switch (sortBy) {
            case "price":
                if(direction.equalsIgnoreCase("asc")) {
                    orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "totalPrice"));
                } else {
                    orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "totalPrice"));
                }
                break;
            case "execDate":
                if(direction.equalsIgnoreCase("asc")) {
                    orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "executionDate"));
                } else {
                    orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "executionDate"));
                }
                break;
            default:
                return new ArrayList<>();
        }

        List<OrderDto> ordersDto = new ArrayList<>();
        for(Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            ordersDto.add(orderDto);
        }
        return ordersDto;
    }

    public List<OrderDto> sortByStatus(int statusCode) {
        List<Order> orders = orderRepository.sortByStatus(statusCode);
        List<OrderDto> ordersDto = new ArrayList<>();
        for(Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            ordersDto.add(orderDto);
        }
        return ordersDto;
    }

    public List<OrderDto> sortForPeriod(String sortBy, String direction, String startDate, String endDate) {
        List<Order> orders;
        switch (sortBy) {
            case "execDate":
                if(direction.equalsIgnoreCase("asc")) {
                    orders = orderRepository.sortByExecDateForPeriodByDateAsc(LocalDate.parse(startDate), LocalDate.parse(endDate));
                } else {
                    orders = orderRepository.sortByExecDateForPeriodByDateDesc(LocalDate.parse(startDate), LocalDate.parse(endDate));
                }
                break;
            case "price":
                if(direction.equalsIgnoreCase("asc")) {
                    orders = orderRepository.sortByExecDateForPeriodByPriceAsc(LocalDate.parse(startDate), LocalDate.parse(endDate));
                } else {
                    orders = orderRepository.sortByExecDateForPeriodByPriceDesc(LocalDate.parse(startDate), LocalDate.parse(endDate));
                }
                break;
            default:
                return new ArrayList<>();
        }

        List<OrderDto> ordersDto = new ArrayList<>();
        for(Order order : orders) {
            OrderDto orderDto = orderMapper.toDto(order);
            ordersDto.add(orderDto);
        }
        return ordersDto;
    }

    public int getEarnedMoneyForPeriod(String startDate, String endDate) {
        List<Order> orders = orderRepository.getCompleteOrdersForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate));

        int sum = 0;
        for(Order order : orders) {
            sum += order.getTotalPrice();
        }

        return sum;
    }

    public int getCountOfCompleteOrdersForPeriod(String startDate, String endDate) {
        return orderRepository
                .getCompleteOrdersForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate))
                .size();
    }
}
