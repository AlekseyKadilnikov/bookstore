package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.ConfigProperty;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.service.IBookService;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Singleton
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @InjectBean
    private IBookService bookService;

    @ConfigProperty()
    private Integer[] months;

    public void sort(List<Book> sortedBooks, Comparator<Book> comparator) {
        sortedBooks = bookService.sort(sortedBooks, comparator);
        System.out.println(sortedBooks.toString());
    }

    public List<Book> getAll() {
        return bookService.getAll();
    }

    public void saveAll(List<Book> bookList) {
        bookService.saveAll(bookList);
    }

    public List<Book> getStaleBooks() {
        return bookService.getOldBooks(months[0]);
    }

    public void showDescription(int bookId) {
        Book book = bookService.getById(bookId);
        if(book != null) {
            System.out.println(book.getDescription());
        } else {
            System.out.println("Book with id = " + bookId + " does not exist!");
        }
    }

    public void writeOff(int bookId) {
        Book book = bookService.getById(bookId);
        book.setCount(0);
        System.out.println(book);
    }

    public void addBook(int bookId, int count){
        bookService.addBook(bookId, count);
        Book book = bookService.getById(bookId);
        System.out.println(book);
    }

    public void importBooks(String path) {
//        int line = 1;
//        try (
//                Reader reader = Files.newBufferedReader(Paths.get(path));
//                CSVReader csvReader = new CSVReader(reader);
//        ) {
//            String[] nextRecord;
//            String name = "";
//            String author = "";
//            String publisher = "";
//            String description = "";
//            int year = 0;
//            int price = 0;
//            int count = 0;
//            long id = 0;
//            LocalDate dateOfReceipt = null;
//            while ((nextRecord = csvReader.readNext()) != null) {
//                for(int i = 0; i < nextRecord.length; i++) {
//                    switch (i) {
//                        case 0:
//                            id = Long.parseLong(nextRecord[i]);
//                            break;
//                        case 1:
//                            name = nextRecord[i];
//                            break;
//                        case 2:
//                            author = nextRecord[i];
//                            break;
//                        case 3:
//                            publisher = nextRecord[i];
//                            break;
//                        case 4:
//                            description = nextRecord[i];
//                            break;
//                        case 5:
//                            year = Integer.parseInt(nextRecord[i].trim());
//                            break;
//                        case 6:
//                            price = Integer.parseInt(nextRecord[i].trim());
//                            break;
//                        case 7:
//                            count = Integer.parseInt(nextRecord[i].trim());
//                            break;
//                        case 8:
//                            dateOfReceipt = LocalDate.parse(nextRecord[i].trim());
//                            break;
//                        default:
//                            break;
//                    }
//                }
//
//                Book book = bookService.getById(id);
//                if (book == null) {
//                    book = new Book(name,author,publisher,year,price,count);
//                    bookService.createBook(book);
//                    book.setDescription(description);
//                    book.setDateOfReceipt(dateOfReceipt);
//                    book.setId(id);
//                    return;
//                }
//                else {
//                    book.setName(name);
//                    book.setAuthor(author);
//                    book.setPublisher(publisher);
//                    book.setDescription(description);
//                    book.setPublicationYear(year);
//                    book.setPrice(price);
//                    book.setCount(count);
//                    book.setDateOfReceipt(dateOfReceipt);
//                }
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

    public void exportBooks(String path, String bookIds) {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(path));
                CSVWriter csvWriter = new CSVWriter(writer);
        ) {
            List<String[]> entries = new ArrayList<>();
            List<Book> books = bookService.getAll();
            if(bookIds.equals("-1")) {
                for(Book book : books) {
                    fillEntry(entries, book);
                }
            }
            else {
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
                    fillEntry(entries, book);
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

    private void fillEntry(List<String[]> entries, Book book) {
//        String[] item = new String[9];
//        item[0] = String.valueOf(book.getId());
//        item[1] = book.getName();
//        item[2] = book.getAuthor();
//        item[3] = book.getPublisher();
//        item[4] = book.getDescription();
//        item[5] = String.valueOf(book.getPublicationYear());
//        item[6] = String.valueOf(book.getCount());
//        item[7] = String.valueOf(book.getPrice());
//        item[8] = String.valueOf(book.getDateOfReceipt());
//        entries.add(item);
    }
}
