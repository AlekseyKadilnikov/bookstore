package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.config.TestConfig;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.service.IBookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class BookControllerTest {

    @Autowired
    private BookController bookController;

    @Autowired
    private IBookService bookService;

    private static List<Book> books;

    @BeforeAll
    public static void setup() {
        books = new ArrayList<>();
    }

    @Test
    public void testFindAll() {

    }
}
