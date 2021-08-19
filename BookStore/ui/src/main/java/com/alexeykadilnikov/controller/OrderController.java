package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.annotation.InjectBean;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.*;
import com.alexeykadilnikov.utils.UserUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private static OrderController instance;

    @InjectBean
    private IOrderService orderService;
    @InjectBean
    private IUserService userService;
    @InjectBean
    private IBookService bookService;

//    private OrderController() {
//    }
//
//    public static OrderController getInstance() {
//        if(instance == null) {
//            instance = new OrderController(OrderService.getInstance());
//        }
//        return instance;
//    }

    public void create(List<Integer> ids) {
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

    public void saveAll(List<Order> orderList) {
        orderService.saveAll(orderList);
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

    public void importOrders(String path) {
        int line = 1;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            long orderId = -1;
            long userId = -1;
            int statusCode = -1;
            LocalDate date = null;
            List<Integer> bookIds = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {
                for(int i = 0; i < nextRecord.length; i++) {
                    if(i == 0) {
                        orderId = Long.parseLong(nextRecord[i].trim());
                    }
                    else if (i == 1) {
                        statusCode = Integer.parseInt(nextRecord[i].trim());
                    }
                    else if (i == 2) {
                        if(!nextRecord[i].equals(" "))
                            date = LocalDate.parse(nextRecord[i].trim());
                    }
                    else if(i == nextRecord.length - 1) {
                        userId = Long.parseLong(nextRecord[i].trim());
                    }
                    else {
                        bookIds.add(Integer.parseInt(nextRecord[i].trim()));
                    }
                }

                User user = userService.getById(userId);
                Order order = orderService.getById(orderId);
                OrderStatus status;
                switch (statusCode) {
                    case 0:
                        status = OrderStatus.NEW;
                        break;
                    case 1:
                        status = OrderStatus.SUCCESS;
                        break;
                    case 2:
                        status = OrderStatus.CANCELED;
                        break;
                    default:
                        logger.error("Invalid status code! (line {})", line);
                        return;
                }

                List<Book> books = new ArrayList<>();
                Book book;
                for (int id : bookIds) {
                    book = bookService.getById(id);
                    if (book == null) {
                        logger.error("Book with id = {} does not exist!", id);
                        return;
                    }
                    books.add(book);
                }

                if(status != OrderStatus.SUCCESS && date != null) {
                    logger.error("There should be no execution date with status code 0 or 2! (line {})", line);
                    return;
                }
                else if(status == OrderStatus.SUCCESS && date == null) {
                    logger.error("Invalid execution date! (line {})", line);
                    return;
                }

                if(user == null) {
                    logger.error("User with id = {} does not exists!", line);
                    return;
                }
                else {
                    if(order == null) {
                        order = new Order(books, userId);
                        order.setStatus(status);
                        order.setExecutionDate(date);
                        order.setId(orderId);
                        orderService.saveOrder(order);
                    }
                    else {
                        order.setBooks(books);
                        order.setUserId(userId);
                        order.setStatus(status);
                        order.setExecutionDate(date);
                        order.setTotalPrice(orderService.calculatePrice(order));
                    }
                }
                bookIds.clear();
                date = null;
                line++;
            }
        }
        catch (IOException e) {
            logger.error("File not found!");
        }
        catch (IndexOutOfBoundsException e) {
            logger.error("Invalid count of parameters! (line {})", line);
        }
        catch (NumberFormatException e) {
            logger.error("Invalid parameter! (line {})", line);
        }
        catch (DateTimeParseException e) {
            logger.error("Invalid date (should be: yyyy-mm-dd)! (line {})", line);
        }
        catch (CsvValidationException e) {
            logger.error("CSV validation error! (line {})", line);
        }
        catch (Exception e) {
            logger.error("Unknown error! (line {})", line);
        }
    }

    public void exportOrders(String path, String orderIds) {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVWriter csvWriter = new CSVWriter(writer);
        ) {
            List<String[]> entries = new ArrayList<>();
            if(orderIds.equals("-1")) {
                List<Order> orders = orderService.getAll();
                for(Order order : orders) {
                    fillEntry(entries, order);
                }
            }
            else {
                String[] idsStr = orderIds.split(" ");
                List<Long> ids = new ArrayList<>();
                for(String idStr : idsStr) {
                    ids.add(Long.parseLong(idStr));
                }
                for(long id : ids) {
                    Order order = orderService.getById(id);
                    if(order == null) {
                        logger.error("Order with id = {} does not exist!", id);
                        return;
                    }
                    fillEntry(entries, order);
                }
            }
            csvWriter.writeAll(entries);
        }
        catch (IOException e) {
            logger.error("File not found!");
        }
        catch (Exception e) {
            logger.error("Unknown error!");
        }
    }

    private void fillEntry(List<String[]> entries, Order order) {
        List<Book> books = order.getBooks();
        String[] item = new String[4 + books.size()];
        item[0] = String.valueOf(order.getId());
        item[1] = String.valueOf(order.getStatus().getStatusCode());
        item[2] = String.valueOf(order.getExecutionDate());
        for(int i = 0; i < books.size(); i++) {
            item[i + 3] = String.valueOf(books.get(i).getId());
        }
        item[item.length - 1] = String.valueOf(order.getUserId());
        entries.add(item);
    }
}
