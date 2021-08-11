package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.RequestService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.*;

public class RequestController {
    private static RequestController instance;

    private static final String CSV_FILE_PATH_READ = "./requestRead.csv";
    private static final String CSV_FILE_PATH_WRITE = "./requestWrite.csv";

    private final RequestService requestService;

    private RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    public static RequestController getInstance() {
        if(instance == null) {
            instance = new RequestController(RequestService.getInstance());
        }
        return instance;
    }

    public void search(String request) {
        Set<Book> foundBooks = requestService.createRequest(request, 1);
        List<Book> books = new ArrayList<>(foundBooks);
        System.out.println(books.toString());
    }

    public void sort(int bookId, Comparator<Request> comparator) {
        BookService bookService = BookService.getInstance();
        Book book = bookService.getByIndex(bookId);
        List<Request> requests = requestService.sort(book, comparator);
        System.out.println(requests.toString());
    }

    public void importRequests() {
        int line = 1;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH_READ));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String name = "";
            int count = 0;
            String[] nextRecord;
            RequestStatus status = null;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (line == 1) {
                    line++;
                    continue;
                }
                for(int i = 0; i < nextRecord.length; i++) {
                    switch (i) {
                        case 0:
                            name = nextRecord[i];
                            break;
                        case 1:
                            count = Integer.parseInt(nextRecord[i].trim());
                            break;
                        default:
                            break;
                    }
                }

                requestService.createRequest(name, count);
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

    public void exportRequests(String bookId) {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(CSV_FILE_PATH_WRITE));
                CSVWriter csvWriter = new CSVWriter(writer);
        ) {
            List<String[]> entries = new ArrayList<>();
            long id = Long.parseLong(bookId.trim());
            BookService bookService = BookService.getInstance();
            Book book = bookService.getById(id);
            if(book == null) {
                System.out.println("Book with id = " + bookId + " does not exist!");
                return;
            }
            for(Request request : book.getCommonRequests()) {
                String[] item = new String[2];
                item[0] = request.getName();
                item[1] = String.valueOf(request.getCount());
                entries.add(item);
            }
            csvWriter.writeAll(entries);
        }
        catch (IOException e) {
            System.out.println("File not found!");
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid book id!");
        }
        catch (Exception e) {
            System.out.println("Unknown error!)");
            //e.printStackTrace();
        }
    }
}
