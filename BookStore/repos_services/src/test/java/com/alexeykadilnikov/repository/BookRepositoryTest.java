package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.config.MySql;
import com.alexeykadilnikov.entity.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = {
        MySql.Initializer.class
})
class BookRepositoryTest {

    @Autowired
    private IBookRepository bookRepository;

    private static Book book;
    private static LocalDate dateOfReceipt;

    @BeforeAll
    static void init() {
        MySql.container.start();

        book = new Book();

        dateOfReceipt = LocalDate.now();
        book.setName("name");
        book.setCount(1);
        book.setPrice(699);
        book.setPublicationYear(1999);
        book.setPublisher("publisher");
        book.setDateOfReceipt(dateOfReceipt);
    }

    @Test
    void getBookById() {
        Optional<Book> book = bookRepository.findById(1L);

        Assertions.assertTrue(book.isPresent());
        Assertions.assertEquals(1, book.get().getId());
    }
}
