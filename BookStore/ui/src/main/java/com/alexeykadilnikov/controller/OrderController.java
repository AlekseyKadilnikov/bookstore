package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Singleton
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @InjectBean
    private IOrderService orderService;
    @InjectBean
    private IUserService userService;
    @InjectBean
    private IBookService bookService;

    public void create(List<Long> ids) {
        orderService.createOrder(ids, UserUtils.getCurrentUser());
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

    public void sortByPrice(int mode) {
        StringBuilder hql = new StringBuilder("from Order order by price ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        List<Order> orders = orderService.sendSqlQuery(hql.toString());

        System.out.println(orders);
    }

    public void sortByExecDate(int mode) {
        StringBuilder hql = new StringBuilder("from Order order by executionDate ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        List<Order> orders = orderService.sendSqlQuery(hql.toString());

        System.out.println(orders);
    }

    public void sortByExecDateForPeriodByDate(String startDate, String endDate, int mode) {
        StringBuilder hql = new StringBuilder("from Order where executionDate >= '" + startDate + "' and executionDate <= '" + endDate + "' order by executionDate ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        List<Order> orders = orderService.sendSqlQuery(hql.toString());
        System.out.println(orders);
    }

    public void sortByExecDateForPeriodByPrice(String startDate, String endDate, int mode) {
        StringBuilder hql = new StringBuilder("from Order where executionDate >= '" + startDate + "' and executionDate <= '" + endDate + "' order by price ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        List<Order> orders = orderService.sendSqlQuery(hql.toString());
        System.out.println(orders);
    }

    public void getEarnedMoneyForPeriod(String startDate, String endDate) {
        List<Order> orders = orderService.sendSqlQuery("from Order where executionDate >= '" + startDate + "' and executionDate <= '" + endDate + "'");

        int sum = 0;
        for(Order order : orders) {
            sum += order.getTotalPrice();
        }

        System.out.println(sum);
    }

    public void getCountOfCompleteOrdersForPeriod(String startDate, String endDate) {
        System.out.println(orderService.sendSqlQuery("from Order where executionDate >= '" + startDate + "' and executionDate <= '" + endDate + "'").size());
    }

    public List<Order> sortByStatus(OrderStatus status) {
        return orderService.sendSqlQuery("from Order where status = " + status.getStatusCode());
    }

    public void setStatus(int orderId, OrderStatus status) {
        orderService.setStatus(orderId, status);
    }


    public void importOrders(String path) {
//        int line = 1;
//        try (
//                Reader reader = Files.newBufferedReader(Paths.get(path));
//                CSVReader csvReader = new CSVReader(reader);
//        ) {
//            String[] nextRecord;
//            long orderId = -1;
//            long userId = -1;
//            int statusCode = -1;
//            LocalDate date = null;
//            List<Integer> bookIds = new ArrayList<>();
//            while ((nextRecord = csvReader.readNext()) != null) {
//                for(int i = 0; i < nextRecord.length; i++) {
//                    if(i == 0) {
//                        orderId = Long.parseLong(nextRecord[i].trim());
//                    }
//                    else if (i == 1) {
//                        statusCode = Integer.parseInt(nextRecord[i].trim());
//                    }
//                    else if (i == 2) {
//                        if(!nextRecord[i].equals(" "))
//                            date = LocalDate.parse(nextRecord[i].trim());
//                    }
//                    else if(i == nextRecord.length - 1) {
//                        userId = Long.parseLong(nextRecord[i].trim());
//                    }
//                    else {
//                        bookIds.add(Integer.parseInt(nextRecord[i].trim()));
//                    }
//                }
//
//                User user = userService.getById(userId);
//                Order order = orderService.getById(orderId);
//                OrderStatus status;
//                switch (statusCode) {
//                    case 0:
//                        status = OrderStatus.NEW;
//                        break;
//                    case 1:
//                        status = OrderStatus.SUCCESS;
//                        break;
//                    case 2:
//                        status = OrderStatus.CANCELED;
//                        break;
//                    default:
//                        logger.error("Invalid status code! (line {})", line);
//                        return;
//                }
//
//                List<Book> books = new ArrayList<>();
//                Book book;
//                for (int id : bookIds) {
//                    book = bookService.getById(id);
//                    if (book == null) {
//                        logger.error("Book with id = {} does not exist!", id);
//                        return;
//                    }
//                    books.add(book);
//                }
//
//                if(status != OrderStatus.SUCCESS && date != null) {
//                    logger.error("There should be no execution date with status code 0 or 2! (line {})", line);
//                    return;
//                }
//                else if(status == OrderStatus.SUCCESS && date == null) {
//                    logger.error("Invalid execution date! (line {})", line);
//                    return;
//                }
//
//                if(user == null) {
//                    logger.error("User with id = {} does not exists!", line);
//                    return;
//                }
//                else {
//                    if(order == null) {
//                        order = new Order(books, userId);
//                        order.setStatus(status);
//                        order.setExecutionDate(date);
//                        order.setId(orderId);
//                        orderService.saveOrder(order);
//                    }
//                    else {
//                        order.setBooks(books);
//                        order.setUserId(userId);
//                        order.setStatus(status);
//                        order.setExecutionDate(date);
//                        order.setTotalPrice(orderService.calculatePrice(order));
//                    }
//                }
//                bookIds.clear();
//                date = null;
//                line++;
//            }
//        }
//        catch (IOException e) {
//            logger.error("File not found!");
//        }
//        catch (IndexOutOfBoundsException e) {
//            logger.error("Invalid count of parameters! (line {})", line);
//        }
//        catch (NumberFormatException e) {
//            logger.error("Invalid parameter! (line {})", line);
//        }
//        catch (DateTimeParseException e) {
//            logger.error("Invalid date (should be: yyyy-mm-dd)! (line {})", line);
//        }
//        catch (CsvValidationException e) {
//            logger.error("CSV validation error! (line {})", line);
//        }
//        catch (Exception e) {
//            logger.error("Unknown error! (line {})", line);
//        }
    }

    public void exportOrders(String path, String orderIds) {
//        try (
//                Writer writer = Files.newBufferedWriter(Paths.get(path));
//                CSVWriter csvWriter = new CSVWriter(writer);
//        ) {
//            List<String[]> entries = new ArrayList<>();
//            if(orderIds.equals("-1")) {
//                List<Order> orders = orderService.getAll();
//                for(Order order : orders) {
//                    fillEntry(entries, order);
//                }
//            }
//            else {
//                String[] idsStr = orderIds.split(" ");
//                List<Long> ids = new ArrayList<>();
//                for(String idStr : idsStr) {
//                    ids.add(Long.parseLong(idStr));
//                }
//                for(long id : ids) {
//                    Order order = orderService.getById(id);
//                    if(order == null) {
//                        logger.error("Order with id = {} does not exist!", id);
//                        return;
//                    }
//                    fillEntry(entries, order);
//                }
//            }
//            csvWriter.writeAll(entries);
//        }
//        catch (IOException e) {
//            logger.error("File not found!");
//        }
//        catch (Exception e) {
//            logger.error("Unknown error!");
//        }
    }

    private void fillEntry(List<String[]> entries, Order order) {
//        List<Book> books = order.getBooks();
//        String[] item = new String[4 + books.size()];
//        item[0] = String.valueOf(order.getId());
//        item[1] = String.valueOf(order.getStatus().getStatusCode());
//        item[2] = String.valueOf(order.getExecutionDate());
//        for(int i = 0; i < books.size(); i++) {
//            item[i + 3] = String.valueOf(books.get(i).getId());
//        }
//        item[item.length - 1] = String.valueOf(order.getUserId());
//        entries.add(item);
    }
}
