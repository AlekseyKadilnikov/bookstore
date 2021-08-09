package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.service.UserService;
import com.alexeykadilnikov.utils.StringUtils;
import com.alexeykadilnikov.utils.UserUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderController {
    private static OrderController instance;

    private final OrderService orderService;

    private static final String CSV_FILE_PATH = "./orders.csv";

    private OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public static OrderController getInstance() {
        if(instance == null) {
            instance = new OrderController(OrderService.getInstance());
        }
        return instance;
    }

    public void create(List<Integer> ids) {
        BookService bookService = BookService.getInstance();
        List<Book> booksAtId = new ArrayList<>();
        for(int id : ids) {
            for(Book book : bookService.getAll()) {
                if(book.getId() == id) {
                    booksAtId.add(book);
                }
            }
        }

        orderService.createOrder(booksAtId, UserUtils.getCurrentUser());
    }

    public void cancel(int id) {
        orderService.cancelOrder(id);
    }

    public List<Order> getAll() {
        return orderService.getAll();
    }

    public void showOne(int id) {
        System.out.println(orderService.showOrder(id));
    }

    public void showAll() {
        List<Order> orders = orderService.getAll();
        for(Order order : orders) {
            System.out.println(order.toString());
        }
    }

    public void sort(List<Order> sortedOrders, Comparator<Order> comparator) {
        sortedOrders = orderService.sort(sortedOrders, comparator);
        System.out.println(sortedOrders.toString());
    }

    public List<Order> sortByStatus(OrderStatus status) {
        List<Order> sortedOrders = new ArrayList<>();
        for(Order order : orderService.getAll()) {
            if(order.getStatus() == status) {
                sortedOrders.add(order);
            }
        }

        return sortedOrders;
    }

    public void getCompletedOrdersForPeriod(Comparator<Order> comparator, String dateAfterS, String dateBeforeS) {
        LocalDate dateAfter = LocalDate.parse(dateAfterS);
        LocalDate dateBefore = LocalDate.parse(dateBeforeS);

        List<Order> orders = sortByStatus(OrderStatus.SUCCESS);
        List<Order> ordersBetweenDates = new ArrayList<>();

        for(Order order : orders) {
            if(order.getExecutionDate().isAfter(dateAfter) && order.getExecutionDate().isBefore(dateBefore)) {
                ordersBetweenDates.add(order);
            }
        }

        sort(ordersBetweenDates, comparator);
    }

    public void setStatus(int orderId, OrderStatus status) {
        Order order = orderService.getByIndex(orderId);
        order.setStatus(status);
        System.out.println(order);
    }

    public void showEarnedMoneyForPeriod(String dateAfterS, String dateBeforeS) {
        int sum = 0;
        LocalDate dateAfter = LocalDate.parse(dateAfterS);
        LocalDate dateBefore = LocalDate.parse(dateBeforeS);
        List<Order> orders = sortByStatus(OrderStatus.SUCCESS);
        for(Order order : orders) {
            if(order.getExecutionDate().isAfter(dateAfter) && order.getExecutionDate().isBefore(dateBefore)) {
                sum += order.getTotalPrice();
            }
        }
        System.out.println("Sum = " + sum);
    }

    public void showCompletedOrdersCountForPeriod(String dateAfterS, String dateBeforeS) {
        int count = 0;
        LocalDate dateAfter = LocalDate.parse(dateAfterS);
        LocalDate dateBefore = LocalDate.parse(dateBeforeS);
        List<Order> orders = sortByStatus(OrderStatus.SUCCESS);
        for(Order order : orders) {
            if(order.getExecutionDate().isAfter(dateAfter) && order.getExecutionDate().isBefore(dateBefore)) {
                count++;
            }
        }
        System.out.println("Count = " + count);
    }

    public void importOrders() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            List<Order> orders = orderService.getAll();
            String[] nextRecord;
            int orderId = -1;
            int userId = -1;
            List<Integer> bookIds = new ArrayList<>();
            int line = 1;
            while ((nextRecord = csvReader.readNext()) != null) {
                if(nextRecord.length < 3) {
                    System.out.println("Not enough parameters! (line " + line + ")");
                    return;
                }
                for(int i = 0; i < nextRecord.length; i++) {
                    if(i == 0) {
                        if(!StringUtils.isNumeric(nextRecord[i])) {
                            System.out.println("Error reading order id! (line " + line + ")");
                            return;
                        }
                        orderId = Integer.parseInt(nextRecord[i]);
                    }
                    else if(i == nextRecord.length - 1) {
                        if(!StringUtils.isNumeric(nextRecord[i])) {
                            System.out.println("Error reading user id! (line " + line + ")");
                            return;
                        }
                        userId = Integer.parseInt(nextRecord[i]);
                    }
                    else {
                        if(!StringUtils.isNumeric(nextRecord[i])) {
                            System.out.println("Error reading book id! (line " + line + ")");
                            return;
                        }
                        bookIds.add(Integer.parseInt(nextRecord[i]));
                    }
                }

                UserService userService = UserService.getInstance();
                BookService bookService = BookService.getInstance();
                User user = userService.getByIndex(userId);
                Order order = orderService.getByIndex(orderId);
                List<Book> books = new ArrayList<>();
                Book book;
                for (int id : bookIds) {
                    book = bookService.getByIndex(id);
                    if (book == null) {
                        System.out.println("Book with id = " + id + " does not exist!");
                        return;
                    }
                    books.add(book);
                }
                if(user == null) {
                    System.out.println("User with id = " + userId + " does not exists!");
                    return;
                }
                else {
                    if(order == null) {
                        orderService.createOrder(books, user);
                    }
                    else {
                        order.setBooks(books);
                        order.setUser(user);
                    }
                }
                bookIds.clear();
                line++;
            }
        }
        catch (IOException e) {
            System.out.println("File not found!");
        }
        catch (CsvValidationException e) {
            System.out.println("CSV validation error!");
        }
        catch (Exception e) {
            System.out.println("Unknown error!");
        }
    }

    public void exportOrders() {

    }
}
