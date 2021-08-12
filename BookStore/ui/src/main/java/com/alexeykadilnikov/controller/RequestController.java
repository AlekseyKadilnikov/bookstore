package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.RequestService;
import com.alexeykadilnikov.utils.StringUtils;
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

    public void importRequests(String path) {
        int line = 1;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            BookService bookService = BookService.getInstance();
            String name = "";
            String status = "";
            int count = 0;
            long id = 0;
            Set<Long> ordersId = new HashSet<>();
            Set<Long> booksId = new HashSet<>();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
//                if (line == 1) {
//                    line++;
//                    continue;
//                }
                for(int i = 0; i < nextRecord.length; i++) {
                    if(i == 0) {
                        id = Long.parseLong(nextRecord[i].trim());
                    } else if(i == 1) {
                        while (StringUtils.isNumeric(nextRecord[i].trim())) {
                            booksId.add(Long.parseLong(nextRecord[i].trim()));
                            i++;
                        }
                        name = nextRecord[i];
                        i++;
                        count = Integer.parseInt(nextRecord[i].trim());
                        i++;
                        status = nextRecord[i].trim();
                    } else {
                        ordersId.add(Long.parseLong(nextRecord[i].trim()));
                    }
                }
                switch (status.toLowerCase()) {
                    case "new":
                        for(long bookId : booksId) {
                            Request request = new Request(name, bookId, ordersId, RequestStatus.NEW);
                            request.setId(id);
                            Book book = bookService.getById(bookId);
                            Request[] orderRequests = book.getOrderRequests();
                            if(orderRequests[0].getId() != id) {
                                System.out.println("Invalid request id! (line " + line + ")");
                            }
                            if(!orderRequests[0].getName().trim().equals(name.trim())) {
                                System.out.println("Invalid name of request! (line " + line + ")");
                                return;
                            }
                            if(book.getCount() != 0) {
                                System.out.println("Invalid status! (line " + line + ")");
                                return;
                            }
                            orderRequests[0].setOrdersId(ordersId);
                            bookService.createRequest(request, count, bookId);
                        }
                        break;
                    case "common":
                        Request req = null;
                        for(long bookId : booksId) {
                            Book book = bookService.getById(bookId);
                            List<Request> common = book.getCommonRequests();
                            long finalId = id;
                            Request request = common.stream()
                                    .filter(r -> r.getId() == finalId)
                                    .findAny()
                                    .orElse(null);
                            if(request == null) {
                                Request r = new Request(name,booksId);
                                r.setId(id);
                                bookService.createRequest(r, count, bookId);
                            }
                            else if(!request.getName().trim().equals(name.trim())) {
                                System.out.println("Invalid name of request! (line " + line + ")");
                                return;
                            } else {
                                req = request;
                            }
                        }
                        if(req != null) {
                            req.setCount(req.getCount() + count);
                        }
                        break;
                    case "success":
                        for(long bookId : booksId) {
                            Request request = new Request(name, bookId, new HashSet<>(), RequestStatus.SUCCESS);
                            request.setId(id);
                            Book book = bookService.getById(bookId);
                            Request[] orderRequests = book.getOrderRequests();
                            if (orderRequests[1].getId() != id) {
                                System.out.println("Invalid request id! (line " + line + ")");
                            }
                            if (!orderRequests[1].getName().trim().equals(name.trim())) {
                                System.out.println("Invalid name of request! (line " + line + ")");
                                return;
                            }
                            orderRequests[0].setOrdersId(ordersId);
                            bookService.createRequest(request, count, bookId);
                        }
                        break;
                    default:
                        break;
                }
                booksId.clear();
                ordersId.clear();
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

    public void exportRequests(String path, String bookIds) {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVWriter csvWriter = new CSVWriter(writer);
        ) {
            BookService bookService = BookService.getInstance();
            List<String[]> entries = new ArrayList<>();
            List<Book> books = bookService.getAll();
            Set<Request> requests = new HashSet<>();
            if(bookIds.equals("-1")) {
                for(Book book : books) {
                    requests.addAll(book.getCommonRequests());
                    requests.addAll(Arrays.asList(book.getOrderRequests()));
                }
                fillEntry(entries, requests);
            } else {
                String[] idsStr = bookIds.split(" ");
                List<Long> ids = new ArrayList<>();
                for(String idStr : idsStr) {
                    ids.add(Long.parseLong(idStr));
                }
                for(long id : ids) {
                    Book book = bookService.getById(id);
                    if(book == null) {
                        System.out.println("Book with id = " + id + " does not exist!");
                        return;
                    }
                    requests.addAll(book.getCommonRequests());
                    requests.addAll(Arrays.asList(book.getOrderRequests()));
                    fillEntry(entries, requests);
                }
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
            System.out.println("Unknown error!");
            e.printStackTrace();
        }
    }

    private void fillEntry(List<String[]> dst, Set<Request> src) {
        for (Request request : src) {
            if(request.getCount() == 0) continue;
            List<String> itemList = new ArrayList<>();
            itemList.add(String.valueOf(request.getId()));
            for(long bookId : request.getBooksId()) {
                itemList.add(String.valueOf(bookId));
            }
            itemList.add(request.getName());
            itemList.add(String.valueOf(request.getCount()));
            itemList.add(String.valueOf(request.getStatus().toString()));

            if(request.getStatus() != RequestStatus.COMMON) {
                for(long orderId : request.getOrdersId()) {
                    itemList.add(String.valueOf(orderId));
                }
            }
            String[] item = new String[itemList.size()];
            item = itemList.toArray(item);
            dst.add(item);
        }
    }
}
