package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final IBookService bookService;

    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/sort/{sortBy}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookDto> sortBy(@PathVariable("sortBy") String sortBy,
                                    @RequestParam("mode") int mode) {
        return bookService.sortBy(sortBy, mode);
    }

    public void getStaleBooksByDate(int mode) {
        bookService.getStaleBooksByDate(mode);
    }

    public void getStaleBooksByPrice(int mode) {
        bookService.getStaleBooksByPrice(mode);
    }

    @PatchMapping("writeOff/{id}")
    public BookDto writeOff(@PathVariable("id") long bookId) {
        return bookService.writeOff(bookId);
    }

    public List<Book> getAll() {
        return bookService.getAll();
    }

    public void saveAll(List<Book> bookList) {
        bookService.saveAll(bookList);
    }

    public void showDescription(Long bookId) {
        System.out.println(bookService.getDescription(bookId));
    }

    public void addBook(long bookId, int count){
        bookService.addBook(bookId, count);
    }

    public void importBooks(String path) {}

    public void exportBooks(String path, String bookIds) {}
}
