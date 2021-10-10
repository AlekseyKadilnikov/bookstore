package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.config.MySql;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.entity.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ContextConfiguration(initializers = {
        MySql.Initializer.class
})
class BookRepositoryTest {

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IAuthorRepository authorRepository;

    private static Book book1;
    private static Book book2;
    private static Book book3;
    private static Book book4;
    private static Author author;

    @BeforeAll
    static void init() {
        MySql.container.start();

        author = new Author("first", "last", "middle");

        book1 = new Book();
        book1.setName("aaa");
        book1.setCount(1);
        book1.setPrice(400);
        book1.setPublicationYear(1999);
        book1.setPublisher("publisher");
        book1.setDateOfReceipt(LocalDate.of(2014, 1, 1));

        book2 = new Book();
        book2.setName("bbb");
        book2.setCount(1);
        book2.setPrice(600);
        book2.setPublicationYear(1999);
        book2.setPublisher("publisher");
        book2.setDateOfReceipt(LocalDate.of(2014, 1, 2));

        book3 = new Book();
        book3.setName("ccc");
        book3.setCount(1);
        book3.setPrice(800);
        book3.setPublicationYear(1999);
        book3.setPublisher("publisher");
        book3.setDateOfReceipt(LocalDate.of(2014, 1, 3));

        book4 = new Book();
        book4.setName("ccc");
        book4.setCount(1);
        book4.setPrice(800);
        book4.setPublicationYear(1999);
        book4.setPublisher("publisher");
        book4.setDateOfReceipt(LocalDate.of(2021, 10, 3));
    }

    @Test
    void getStateBooksOrderByNameAsc() {
        bookRepository.deleteAll();

        bookRepository.save(book4);
        bookRepository.save(book3);
        bookRepository.save(book2);
        bookRepository.save(book1);

        List<Book> booksReturned = bookRepository.getStateBooksOrderByNameAsc(LocalDate.of(2021, 1, 1));

        Assertions.assertEquals(3, booksReturned.size());
        Assertions.assertEquals(book1.getName(), booksReturned.get(0).getName());
        Assertions.assertEquals(book2.getName(), booksReturned.get(1).getName());
        Assertions.assertEquals(book3.getName(), booksReturned.get(2).getName());
    }

    @Test
    void getStateBooksOrderByNameDesc() {
        bookRepository.deleteAll();

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);

        List<Book> booksReturned = bookRepository.getStateBooksOrderByNameDesc(LocalDate.of(2021, 1, 1));

        Assertions.assertEquals(3, booksReturned.size());
        Assertions.assertEquals(book3.getName(), booksReturned.get(0).getName());
        Assertions.assertEquals(book2.getName(), booksReturned.get(1).getName());
        Assertions.assertEquals(book1.getName(), booksReturned.get(2).getName());
    }

    @Test
    void getStateBooksOrderByPriceAsc() {
        bookRepository.deleteAll();

        bookRepository.save(book4);
        bookRepository.save(book3);
        bookRepository.save(book2);
        bookRepository.save(book1);

        List<Book> booksReturned = bookRepository.getStateBooksOrderByPriceAsc(LocalDate.of(2021, 1, 1));

        Assertions.assertEquals(3, booksReturned.size());
        Assertions.assertEquals(book1.getPrice(), booksReturned.get(0).getPrice());
        Assertions.assertEquals(book2.getPrice(), booksReturned.get(1).getPrice());
        Assertions.assertEquals(book3.getPrice(), booksReturned.get(2).getPrice());
    }

    @Test
    void getStateBooksOrderByPriceDesc() {
        bookRepository.deleteAll();

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);

        List<Book> booksReturned = bookRepository.getStateBooksOrderByPriceDesc(LocalDate.of(2021, 1, 1));

        Assertions.assertEquals(3, booksReturned.size());
        Assertions.assertEquals(book3.getPrice(), booksReturned.get(0).getPrice());
        Assertions.assertEquals(book2.getPrice(), booksReturned.get(1).getPrice());
        Assertions.assertEquals(book1.getPrice(), booksReturned.get(2).getPrice());
    }

    @Test
    void saveBook_and_getBookById() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        authorRepository.save(author);
        book1.setAuthors(Collections.singleton(author));

        Book book = bookRepository.save(book1);

        Optional<Book> bookReturned = bookRepository.findById(book.getId());

        Assertions.assertTrue(bookReturned.isPresent());
        Assertions.assertEquals(book.getId(), bookReturned.get().getId());
        Assertions.assertNotNull(book.getAuthors());
    }

    @Test
    void saveBook_and_deleteBook() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        Author savedAuthor = authorRepository.save(author);
        book1.setAuthors(Collections.singleton(savedAuthor));
        Book book = bookRepository.save(book1);
        bookRepository.delete(book);

        int size = bookRepository.findAll().size();

        Assertions.assertEquals(0, size);
    }

    @Test
    void saveBook_and_findAllBooks() {
        bookRepository.deleteAll();

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();

        Assertions.assertEquals(2, books.size());
    }

    @Test
    void saveBook_and_updateBook() {
        bookRepository.deleteAll();

        Book returned = bookRepository.save(book1);

        Assertions.assertNotNull(returned);

        long idBefore = returned.getId();

        returned.setPrice(401);
        returned = bookRepository.save(returned);

        Assertions.assertNotNull(returned);

        long idAfter = returned.getId();

        Assertions.assertEquals(idBefore, idAfter);
        Assertions.assertEquals(401, returned.getPrice());
    }
}
