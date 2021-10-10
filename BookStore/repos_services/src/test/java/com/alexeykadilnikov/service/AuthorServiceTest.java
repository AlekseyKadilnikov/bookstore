package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.AuthorDto;
import com.alexeykadilnikov.dto.BookDto;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.mapper.AuthorMapper;
import com.alexeykadilnikov.mapper.BookMapper;
import com.alexeykadilnikov.repository.IAuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthorServiceTest {

    private IAuthorService authorService;

    @MockBean
    private IAuthorRepository authorRepository;

    long authorId = 1L;

    @BeforeAll
    void init(){

        AuthorMapper authorMapper = new AuthorMapper(new ModelMapper());

        authorService = new AuthorService(authorRepository, authorMapper);
    }

    @Test
    void shouldFetchAllBooks() {
        List<Author> testAuthors = new ArrayList<>();
        testAuthors.add(new Author("first", "last", "middle"));
        testAuthors.add(new Author("first", "last", "middle"));
        testAuthors.add(new Author("first", "last", "middle"));

        List<AuthorDto> testAuthorsDto = new ArrayList<>();
        testAuthorsDto.add(new AuthorDto("first", "last"));
        testAuthorsDto.add(new AuthorDto("first", "last"));
        testAuthorsDto.add(new AuthorDto("first", "last"));

        given(authorRepository.findAll()).willReturn(testAuthors);

        List<AuthorDto> result = authorService.findAll();

        Assertions.assertIterableEquals(testAuthorsDto, result);
    }

    @Test
    void shouldFetchOneBookById() {
        Author author = new Author("first", "last", "middle");
        author.setId(authorId);

        given(authorRepository.findById(1L)).willReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getById(authorId);

        Assertions.assertEquals(authorId, authorDto.getId());
    }
}
