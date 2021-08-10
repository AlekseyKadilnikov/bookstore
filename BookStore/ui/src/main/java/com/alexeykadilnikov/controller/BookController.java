package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.BookService;
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
import java.util.Comparator;
import java.util.List;

public class BookController {
    private static BookController instance;

    private static final String CSV_FILE_PATH_READ = "./booksRead.csv";
    private static final String CSV_FILE_PATH_WRITE = "./booksWrite.csv";

    private final BookService bookService;

    private BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public static BookController getInstance() {
        if(instance == null) {
            instance = new BookController(BookService.getInstance());
        }
        return instance;
    }

    public void sort(List<Book> sortedBooks, Comparator<Book> comparator) {
        sortedBooks = bookService.sort(sortedBooks, comparator);
        System.out.println(sortedBooks.toString());
    }

    public List<Book> getAll() {
        return bookService.getAll();
    }

    public List<Book> getStaleBooks(int months) {
        return bookService.getOldBooks(months);
    }

    public void showDescription(int bookId) {
        Book book = bookService.getByIndex(bookId);
        System.out.println(book.getDescription());
    }

    public void writeOff(int bookId) {
        Book book = bookService.getByIndex(bookId);
        book.setCount(0);
        System.out.println(book);
    }

    public void addBook(int bookId, int count){
        bookService.addBook(bookId, count);
        Book book = bookService.getByIndex(bookId);
        System.out.println(book);
    }

    public void importBooks() {
        int line = 1;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH_READ));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int bookId = 0;
            String name = "", author = "", publisher = "";
            int year = 0, price = 0, count = 0;
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
                            year = Integer.parseInt(nextRecord[i].trim());
                            break;
                        case 5:
                            price = Integer.parseInt(nextRecord[i].trim());
                            break;
                        case 6:
                            count = Integer.parseInt(nextRecord[i].trim());
                            break;
                        case 7:
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

    public void exportBooks(String bookIds) {
//        int line = 1;
//        try (
//                Writer writer = Files.newBufferedWriter(Paths.get(CSV_FILE_PATH_WRITE));
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
//                    fillEntry(entries, order);
//                }
//            }
//            csvWriter.writeAll(entries);
//        }
//        catch (IOException e) {
//            System.out.println("File not found!");
//        }
//        catch (Exception e) {
//            System.out.println("Unknown error! (line " + line + ")");
//            //e.printStackTrace();
//        }
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
        item[item.length - 1] = String.valueOf(order.getUser().getId());
        entries.add(item);
    }
}
