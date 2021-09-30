package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.config.MySql;
import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;


@SpringBootTest
@ContextConfiguration(initializers = {
        MySql.Initializer.class
})
class BookRepositoryTest {

    @Autowired
    private IBookRepository bookRepository;

    @BeforeAll
    static void init() {
        MySql.container.start();
    }

    @Test
    void getAllTest() {
        List<Book> books = bookRepository.findAll();

        assertThat(books.size(), 0);
    }

}
