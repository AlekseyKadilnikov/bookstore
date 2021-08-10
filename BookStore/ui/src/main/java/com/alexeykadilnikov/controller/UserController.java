package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.UserService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
            String[] nextRecord;
            String name = "";
            String author = "";
            String publisher = "";
            String description = "";
            int year = 0;
            int price = 0;
            int count = 0;
            long id = 0;
            LocalDate dateOfReceipt = null;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (line == 1) {
                    line++;
                    continue;
                }
                for(int i = 0; i < nextRecord.length; i++) {
                    switch (i) {
                        case 0:
                            id = Long.parseLong(nextRecord[i]);
                            break;
                        case 1:
                            name = nextRecord[i];
                            break;
                        case 2:
                            author = nextRecord[i];
                            break;
                        case 3:
                            publisher = nextRecord[i];
                            break;
                        case 4:
                            description = nextRecord[i];
                            break;
                        case 5:
                            year = Integer.parseInt(nextRecord[i].trim());
                            break;
                        case 6:
                            price = Integer.parseInt(nextRecord[i].trim());
                            break;
                        case 7:
                            count = Integer.parseInt(nextRecord[i].trim());
                            break;
                        case 8:
                            dateOfReceipt = LocalDate.parse(nextRecord[i].trim());
                            break;
                        default:
                            break;
                    }
                }

                Book book = bookService.getById(id);
                if (book == null) {
                    System.out.println("Book with id = " + id + " does not exist!");
                    return;
                }
                else {
                    book.setName(name);
                    book.setAuthor(author);
                    book.setPublisher(publisher);
                    book.setDescription(description);
                    book.setPublicationYear(year);
                    book.setPrice(price);
                    book.setCount(count);
                    book.setDateOfReceipt(dateOfReceipt);
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

    public void exportUsers(String ids) {

    }
}
