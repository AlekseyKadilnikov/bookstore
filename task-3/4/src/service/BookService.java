package service;

import model.Book;

public class BookService implements IBookService {
    private Book[] books;

    public BookService() {
    }

    public BookService(Book[] books) {
        this.books = books;
    }

    @Override
    public void addBook(Book book) {

    }

    @Override
    public Book[] findAll() {
        return books;
    }

    @Override
    public Book findById(int id) {
        return null;
    }

    @Override
    public Book findByName(String name) {
        return null;
    }

    @Override
    public Book findByAuthor(String author) {
        return null;
    }
}
