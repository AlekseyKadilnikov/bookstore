package com.alexeykadilnikov.service;

import com.alexeykadilnikov.config.TestConfig;
import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.mapper.BookMapper;
import com.alexeykadilnikov.repository.IBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
class BookServiceTest {

    private IBookService bookService;

    @Mock
    private IBookRepository bookRepository;

    long bookId = 1L;

    @BeforeAll
    void init(){

        BookMapper bookMapper = new BookMapper(new ModelMapper());

        bookService = new BookService(bookRepository, bookMapper);
    }

    @Test
    void shouldFetchAllBooks() {
        List<Book> testBooks = new ArrayList<>();
        testBooks.add(new Book("book1", 2010, 700, 6, "d1", new HashSet<>()));
        testBooks.add(new Book("book2", 2016, 530, 30, "d2", new HashSet<>()));
        testBooks.add(new Book("book3", 2012, 540, 25, "d3", new HashSet<>()));

        List<BookDto> testBooksDto = new ArrayList<>();
        testBooksDto.add(new BookDto("book1", 2010, 700, 6, "d1"));
        testBooksDto.add(new BookDto("book2", 2016, 530, 30, "d2"));
        testBooksDto.add(new BookDto("book3", 2012, 540, 25, "d3"));

        when(bookRepository.findAll()).thenReturn(testBooks);

        List<BookDto> result = bookService.getAll();

        Assertions.assertIterableEquals(testBooksDto, result);
    }

    @Test
    void shouldFetchOneBookById() {
        Book book = new Book("book2", 2016, 530, 10, "d2", new HashSet<>());
        book.setId(bookId);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getById(bookId);

        Assertions.assertEquals(bookId, bookDto.getId());
    }

    @Test
    void getBookDescriptionById() {
        Book book = new Book("book2", 2016, 530, 10, "d2", new HashSet<>());
        book.setId(bookId);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        String description = bookService.getDescription(bookId);

        Assertions.assertEquals("d2", description);
    }

    @Test
    void writeOffBookTest() {
        Book book = new Book("book2", 2016, 530, 10, "d2", new HashSet<>());
        book.setId(bookId);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.writeOff(bookId);

        Assertions.assertEquals(0, bookDto.getCount());
    }

    @Test
    void addBookTest() {
        Book book = new Book("book2", 2016, 530, 0, "d2", new HashSet<>());
        book.setId(bookId);

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.addBook(bookId, 10);

        Assertions.assertEquals(10, bookDto.getCount());
    }
}