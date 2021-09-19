package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/sort/{sortBy}")
    public List<BookDto> sortBy(@PathVariable("sortBy") String sortBy,
                                    @RequestParam("mode") int mode) {
        return bookService.sortBy(sortBy, mode);
    }

    @GetMapping(value = "/stale/{sortBy}")
    public List<BookDto> getStaleBooks(@PathVariable("sortBy") String sortBy,
                                    @RequestParam("mode") int mode) {
        return bookService.getStaleBooks(sortBy, mode);
    }

    @PatchMapping("writeOff/{id}")
    public BookDto writeOff(@PathVariable("id") long bookId) {
        return bookService.writeOff(bookId);
    }

    @GetMapping()
    public List<BookDto> getAll() {
        return bookService.getAll();
    }

    @GetMapping("{id}/description")
    public String getDescription(@PathVariable("id") Long bookId) {
        return bookService.getDescription(bookId);
    }

    @PatchMapping("add/{id}")
    public BookDto addBook(@PathVariable("id") long bookId,
                        @RequestParam("count") int count) {
        return bookService.addBook(bookId, count);
    }
}
