package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.service.IBookService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@NoArgsConstructor
@RequestMapping("/books")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private IBookService bookService;

    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/sort/{sortBy}")
    @PreAuthorize("hasAuthority('books:read')")
    public List<BookDto> sortBy(@PathVariable("sortBy") String sortBy,
                                    @RequestParam("direction") String direction) {
        return bookService.sortBy(sortBy, direction);
    }

    @GetMapping(value = "/stale/{sortBy}")
    @PreAuthorize("hasAuthority('books:read')")
    public List<BookDto> getStaleBooks(@PathVariable("sortBy") String sortBy,
                                    @RequestParam("direction") String direction) {
        return bookService.getStaleBooks(sortBy, direction);
    }

    @PatchMapping("/writeOff/{id}")
    @PreAuthorize("hasAuthority('books:update')")
    public BookDto writeOff(@PathVariable("id") long bookId) {
        return bookService.writeOff(bookId);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('books:read')")
    public List<BookDto> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}/description")
    @PreAuthorize("hasAuthority('books:read')")
    public String getDescription(@PathVariable("id") Long bookId) {
        return bookService.getDescription(bookId);
    }

    @PatchMapping("add/{id}")
    @PreAuthorize("hasAuthority('books:write')")
    public BookDto addBook(@PathVariable("id") long bookId,
                        @RequestParam("count") int count) {
        return bookService.addBook(bookId, count);
    }
}
