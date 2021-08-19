package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.annotation.InjectBean;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.IBookService;
import com.alexeykadilnikov.service.IRequestService;
import com.alexeykadilnikov.service.RequestService;
import com.alexeykadilnikov.utils.StringUtils;
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

public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    private static RequestController instance;

    @InjectBean
    private IRequestService requestService;
    @InjectBean
    private IBookService bookService;

//    private RequestController(RequestService requestService) {
//        this.requestService = requestService;
//    }
//
//    public static RequestController getInstance() {
//        if(instance == null) {
//            instance = new RequestController(RequestService.getInstance());
//        }
//        return instance;
//    }

    public void search(String request) {
        Set<Book> foundBooks = requestService.createRequest(request, 1);
        List<Book> books = new ArrayList<>(foundBooks);
        System.out.println(books);
    }

    public void sort(int bookId, Comparator<Request> comparator) {
        Book book = bookService.getById(bookId);
        List<Request> requests = requestService.sort(book, comparator);
        System.out.println(requests.toString());
    }

    public List<Request> getAll() {
        return requestService.getAll();
    }

    public void saveAll(List<Request> requestList) {
        requestService.saveAll(requestList);
    }

    public void importRequests(String path) {
        int line = 1;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String name = "";
            String status = "";
            int count = 0;
            long id = 0;
            Set<Long> ordersId = new HashSet<>();
            Set<Long> booksId = new HashSet<>();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
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
                                logger.error("Invalid request id! (line {})", line);
                            }
                            if(!orderRequests[0].getName().trim().equals(name.trim())) {
                                logger.error("Invalid name of request! (line {})", line);
                                return;
                            }
                            if(book.getCount() != 0) {
                                logger.error("Invalid status! (line {})", line);
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
                                logger.error("Invalid name of request! (line {})", line);
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
                                logger.error("Invalid request id! (line {})", line);
                            }
                            if (!orderRequests[1].getName().trim().equals(name.trim())) {
                                logger.error("Invalid name of request! (line {})", line);
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

    public void exportRequests(String path, String bookIds) {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVWriter csvWriter = new CSVWriter(writer);
        ) {
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
                        logger.error("Book with id = {} does not exist!", id);
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
            logger.error("File not found!");
        }
        catch (NumberFormatException e) {
            logger.error("Invalid book id!");
        }
        catch (Exception e) {
            logger.error("Unknown error!");
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
