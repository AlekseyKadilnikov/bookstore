package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.service.UserService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.*;

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static UserController instance;

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    public int create(String username) {
        if(userService.addUser(username) > 0) {
            return 1;
        }
        return 0;
    }

    public User getOne(String username) {
        return userService.getByName(username);
    }

    public void getOrders(User user) {
        for(Order order : user.getOrders()) {
            System.out.println(order.toString());
        }
    }

    public static UserController getInstance() {
        if(instance == null) {
            instance = new UserController(UserService.getInstance());
        }
        return instance;
    }

    public void importUsers(String path) {
        int line = 1;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            long id = 0;
            String name = "";
            String[] nextRecord;
            List<Long> ordersId = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {
                for(int i = 0; i < nextRecord.length; i++) {
                    switch (i) {
                        case 0:
                            id = Long.parseLong(nextRecord[i].trim());
                            break;
                        case 1:
                            name = nextRecord[i];
                            break;
                        default:
                            ordersId.add(Long.parseLong(nextRecord[i].trim()));
                            break;
                    }
                }

                User user = userService.getById(id);
                Set<Order> orders = new HashSet<>();
                OrderService orderService = OrderService.getInstance();
                for(Long orderId : ordersId) {
                    Order order = orderService.getById(orderId);
                    if(order == null) {
                        logger.error("Order with id = {} does not exist! (line {})", id, line);
                        return;
                    }
                    orders.add(order);
                }

                if (user == null) {
                    user = new User(name);
                    user.setId(id);
                    user.setOrders(orders);
                    userService.addUser(user);
                }
                else {
                    user.setUsername(name);
                    user.getOrders().addAll(orders);
                }
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

    public void exportUsers(String path, String userIds) {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVWriter csvWriter = new CSVWriter(writer);
        ) {
            List<String[]> entries = new ArrayList<>();
            List<User> users = userService.getAll();
            if(userIds.equals("-1")) {
                for(User user : users) {
                    fillEntry(entries, user);
                }
            }
            else {
                String[] idsStr = userIds.split(" ");
                List<Long> ids = new ArrayList<>();
                for(String idStr : idsStr) {
                    ids.add(Long.parseLong(idStr));
                }
                for(long id : ids) {
                    User user = userService.getById(id);
                    if(user == null) {
                        logger.error("User with id = {} does not exist!", id);
                        return;
                    }
                    fillEntry(entries, user);
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

    private void fillEntry(List<String[]> entries, User user) {
        Set<Order> orders = user.getOrders();
        String[] item = new String[2 + user.getOrders().size()];
        item[0] = String.valueOf(user.getId());
        item[1] = user.getUsername();
        Iterator<Order> iterator = orders.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            item[i + 2] = String.valueOf(iterator.next().getId());
            i++;
        }

        entries.add(item);
    }
}