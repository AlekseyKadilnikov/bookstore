package service;

import model.Book;

public interface IBookService {
    void addBook(Book book);
    Book[] findAll();
    Book findById(int id);
    Book findByName(String name);
    Book findByAuthor(String author);
}
