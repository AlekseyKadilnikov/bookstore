package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.dao.IAuthorDao;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class AuthorRepository implements IAuthorRepository {
    private static final Logger logger = LoggerFactory.getLogger(AuthorRepository.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @InjectBean
    private IAuthorDao authorDao;

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();
            authors = authorDao.findAll();

            for(Author author : authors) {
                setBooksForAuthor(author, statement);
            }

        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return authors;
    }

    @Override
    public Author getById(Long id) {
        Author author = new Author();

        try {
            author = authorDao.getById(id);

            Statement statement = DBUtils.getConnection().createStatement();
            setBooksForAuthor(author, statement);

            logger.info("Get author with id = {}", author.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return author;
    }

    @Override
    public void save(Author author) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBUtils.getConnection();
            savepoint = connection.setSavepoint();
            createAndExecuteQueryForSavingAuthor(author);
        } catch (SQLException e) {
            if(connection != null && savepoint != null) {
                try {
                    connection.rollback(savepoint);
                } catch (SQLException ex) {
                    logger.error(SQL_EX_MESSAGE, ex);
                }
            }
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void delete(Author author) {
        authorDao.delete(author.getId());
    }

    @Override
    public void saveAll(List<Author> all) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBUtils.getConnection();
            savepoint = connection.setSavepoint();
            for(Author author : all) {
                createAndExecuteQueryForSavingAuthor(author);
            }
        } catch (SQLException e) {
            if(connection != null && savepoint != null) {
                try {
                    connection.rollback(savepoint);
                } catch (SQLException ex) {
                    logger.error(SQL_EX_MESSAGE, ex);
                }
            }
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    private void createAndExecuteQueryForSavingAuthor(Author author) throws SQLException, IOException {
        authorDao.save(author);

        PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement(
                "INSERT INTO author_book (author_id, book_id)" +
                        "VALUES (?, ?)");
        for(long bookId : author.getBooksId()) {
            prepStatement.setLong(1, author.getId());
            prepStatement.setLong(2, bookId);
            prepStatement.executeUpdate();
        }

        DBUtils.getConnection().commit();
        logger.info("Author with id = {} was saved", author.getId());
    }

    private void setBooksForAuthor(Author author, Statement statement) throws SQLException, IOException {
        ResultSet resultSetBook = statement.executeQuery("SELECT * FROM author_book WHERE author_id = " + author.getId());
        Set<Long> booksId = new HashSet<>();
        while (resultSetBook.next()) {
            booksId.add(resultSetBook.getLong("book_id"));
        }
        author.setBooksId(booksId);
    }
}
