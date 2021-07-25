package service;

import repository.BookRepository;

public class BookService implements IBookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
