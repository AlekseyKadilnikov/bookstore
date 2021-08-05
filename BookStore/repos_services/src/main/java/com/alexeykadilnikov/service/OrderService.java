package com.alexeykadilnikov.service;

import com.alexeykadilnikov.OrderComparator;
import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.*;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;

import java.util.*;

public class OrderService implements IOrderService {
    private static OrderService instance;

    private final OrderRepository orderRepository;

    private OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void createOrder(List<Book> books, User user) {
        for (Book book : books) {
            if(book.getCount() == 0) {
                book.addRequest(new Request("Request for " + book.getAuthor() + " - " + book.getName(), RequestStatus.NEW));
                System.out.println("Common request for " + book.getAuthor() + " - " + book.getName() + " created");
                book.addRequest(new Request("Request for " + book.getAuthor() + " - " + book.getName(), RequestStatus.COMMON));
                System.out.println("Order request for " + book.getAuthor() + " - " + book.getName() + " created");
            }
            else {
                book.setCount(book.getCount() - 1);
            }
        }
        Order order = new Order(books, user);
        orderRepository.save(order);
        System.out.println("Order id = " + order.getId() + " created");
    }

    @Override
    public String showOrder(int index) {
        return orderRepository.getByIndex(index).toString();
    }

    @Override
    public void cancelOrder(int id) {
        Order order = orderRepository.getByIndex(id);
        for(Book book : order.getBooks()) {
            book.setCount(book.getCount() + 1);
            List<Request> orderRequests = book.getOrderRequests();
            for(Request request : orderRequests) {
                if(request.getStatus() == RequestStatus.NEW) {
                    book.addRequest(new Request(request.getName(), RequestStatus.SUCCESS));
                    if(request.getCount() > 0) {
                        request.setCount(request.getCount() - 1);
                    }
                    else {
                        orderRequests.remove(request);
                    }
                }
            }
        }
        order.setStatus(OrderStatus.CANCELED);
        System.out.println("Order id = " + order.getId() + " canceled\n");
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
                    System.out.println("Order id = " + order.getId() + " couldn't be completed: request for " +
                            book.getAuthor() + " - " + book.getName() + " not closed");
                    return;
                }
            }
        }
        order.setStatus(OrderStatus.SUCCESS);
        Calendar calendar = new GregorianCalendar();
        Random random = new Random();
        calendar.add(Calendar.DAY_OF_WEEK, random.nextInt(4));
        Date date = calendar.getTime();
        order.setExecutionDate(date);
        System.out.println("Order id = " + order.getId() + " completed");
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

    public Order[] getOrderListForPeriod(Date dateAfter, Date dateBefore) {
        Order[] orders = new Order[0];
        for(Order order : orderRepository.findAll()){
                if(order.getStatus() == OrderStatus.SUCCESS &&
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

    public int getAmountOfCompletedOrdersForPeriod(Date dateAfter, Date dateBefore) {
        int count = 0;
        for(Order order : orderRepository.findAll()) {
            if(order.getStatus() == OrderStatus.SUCCESS &&
                    order.getExecutionDate().after(dateAfter) &&
                    order.getExecutionDate().before(dateBefore)) {
                count++;
            }
        }
        return count;
    }

    public int getAmountOfMoneyForPeriod(Date dateAfter, Date dateBefore) {
        int money = 0;
        for(Order order : orderRepository.findAll()) {
            if(order.getStatus() == OrderStatus.SUCCESS &&
                    order.getExecutionDate().after(dateAfter) &&
                    order.getExecutionDate().before(dateBefore)) {
                money += order.getPrice();
            }
        }
        return money;
    }

    public Order[] sortByExecutionDateAscending(Order[] orders) {
        List<Order> successOrders = new ArrayList<>();
        for(Order order : orders) {
            if(order.getStatus() == OrderStatus.SUCCESS) {
                successOrders.add(order);
            }
        }
        successOrders.sort(OrderComparator.DateComparatorAscending);
        Order[] successOrdersArr = new Order[successOrders.size()];
        for(int i = 0; i < successOrders.size(); i++) {
            successOrdersArr[i] = successOrders.get(i);
        }
        return successOrdersArr;
    }

    public Order[] sortByExecutionDateDescending(Order[] orders) {
        List<Order> successOrders = new ArrayList<>();
        for(Order order : orders) {
            if(order.getStatus() == OrderStatus.SUCCESS) {
                successOrders.add(order);
            }
        }
        successOrders.sort(OrderComparator.DateComparatorDescending);
        Order[] successOrdersArr = new Order[successOrders.size()];
        for(int i = 0; i < successOrders.size(); i++) {
            successOrdersArr[i] = successOrders.get(i);
        }
        return successOrdersArr;
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
