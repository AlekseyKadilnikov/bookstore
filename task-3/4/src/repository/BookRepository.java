package repository;

import model.Book;

import java.util.List;

public class BookRepository implements IRepository<Book, Long> {
    private Book[] books = new Book[] {
            new Book("name1", "author1", "publisher1", 200, true),
            new Book("name2", "author2", "publisher2", 300, false),
            new Book("name3", "author3", "publisher1", 200, false),
            new Book("name4", "author4", "publisher2", 500, true),
            new Book("name5", "author5", "publisher1", 600, true)
    };

    @Override
    public List<Book> findAll() {
        return null;
    }

    @Override
    public Book getById(Long id) {
        return null;
    }

    @Override
    public void save(Book book) {

    }

    @Override
    public void delete(Book book) {

    }

    public Book getByIndex(int index) {
        return books[index];
    }
}
