package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.UserService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private static UserController instance;

    private static final String CSV_FILE_PATH_READ = "./userRead.csv";
    private static final String CSV_FILE_PATH_WRITE = "./userWrite.csv";

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

    public void importUsers() {
        int line = 1;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH_READ));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            long id = 0;
            String name = "";
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (line == 1) {
                    line++;
                    continue;
                }
                for(int i = 0; i < nextRecord.length; i++) {
                    switch (i) {
                        case 0:
                            id = Long.parseLong(nextRecord[i].trim());
                            break;
                        case 1:
                            name = nextRecord[i];
                            break;
                        default:
                            break;
                    }
                }

                User user = userService.getById(id);
                if (user == null) {
                    user = new User(name);
                    user.setId(id);
                    userService.addUser(user);
                }
                else {
                    user.setUsername(name);
                }
                line++;
            }
        }
        catch (IOException e) {
            System.out.println("File not found!");
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid count of parameters! (line " + line + ")");
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid parameter! (line " + line + ")");
        }
        catch (DateTimeParseException e) {
            System.out.println("Invalid date (should be: yyyy-mm-dd)! (line " + line + ")");
        }
        catch (CsvValidationException e) {
            System.out.println("CSV validation error! (line " + line + ")");
        }
        catch (Exception e) {
            System.out.println("Unknown error! (line " + line + ")");
            //e.printStackTrace();
        }
    }

    public void exportUsers(String userIds) {
        int line = 1;
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(CSV_FILE_PATH_WRITE));
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
                        System.out.println("User with id = " + id + " does not exist!");
                        return;
                    }
                    fillEntry(entries, user);
                }
            }
            csvWriter.writeAll(entries);
        }
        catch (IOException e) {
            System.out.println("File not found!");
        }
        catch (Exception e) {
            System.out.println("Unknown error! (line " + line + ")");
            //e.printStackTrace();
        }
    }

    private void fillEntry(List<String[]> entries, User user) {
        String[] item = new String[2];
        item[0] = String.valueOf(user.getId());
        item[1] = user.getUsername();
        entries.add(item);
    }
}
