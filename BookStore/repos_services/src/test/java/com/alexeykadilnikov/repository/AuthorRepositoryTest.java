package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.config.MySql;
import com.alexeykadilnikov.entity.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ContextConfiguration(initializers = {
        MySql.Initializer.class
})
class AuthorRepositoryTest {
    @Autowired
    private IAuthorRepository authorRepository;

    @BeforeAll
    static void init() {
        MySql.container.start();
    }

    @Test
    void saveAuthor_and_getAuthorById() {
        authorRepository.deleteAll();

        Author author = new Author();
        author.setFirstName("first");
        author.setLastName("last");
        author.setMiddleName("middle");

        authorRepository.save(author);

        Optional<Author> returned = authorRepository.findById(1L);

        Assertions.assertTrue(returned.isPresent());
        Assertions.assertEquals(author.getId(), returned.get().getId());
    }

    @Test
    void saveAuthor_and_deleteAuthor() {
        authorRepository.deleteAll();

        Author author = new Author();
        author.setFirstName("first");
        author.setLastName("last");
        author.setMiddleName("middle");

        authorRepository.save(author);
        authorRepository.delete(author);

        int size = authorRepository.findAll().size();

        Assertions.assertEquals(0, size);
    }

    @Test
    void saveAuthor_and_findAllAuthors() {
        authorRepository.deleteAll();

        Author authorOne = new Author("first", "last", "middle");
        Author authorTwo = new Author("first", "last", "middle");
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);

        List<Author> authors = authorRepository.findAll();

        Assertions.assertEquals(2, authors.size());
    }

    @Test
    void save_and_updateAuthor() {
        authorRepository.deleteAll();

        Author author = new Author();
        author.setFirstName("first");
        author.setLastName("last");
        author.setMiddleName("middle");


        Author returned = authorRepository.save(author);

        Assertions.assertNotNull(returned);

        long idBefore = returned.getId();

        author.setLastName("renamed");
        returned = authorRepository.save(author);

        Assertions.assertNotNull(returned);

        long idAfter = returned.getId();
        String nameAfter = returned.getLastName();

        Assertions.assertEquals(idBefore, idAfter);
        Assertions.assertEquals(author.getLastName(), nameAfter);
    }
}
