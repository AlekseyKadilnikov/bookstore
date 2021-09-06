package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AuthorDao implements IAuthorDao {
    private static final Logger logger = LoggerFactory.getLogger(AuthorDao.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM author");
            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
                author.setMiddleName(resultSet.getString("middle_name"));
                authors.add(author);
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
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("SELECT * FROM author WHERE id = ?");
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            author.setId(resultSet.getInt("id"));
            author.setFirstName(resultSet.getString("first_name"));
            author.setLastName(resultSet.getString("last_name"));
            author.setMiddleName(resultSet.getString("middle_name"));

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
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement(
                    "INSERT INTO author (id, first_name, last_name, middle_name)" +
                            "VALUES (?, ?, ?, ?)");
            prepStatement.setLong(1, author.getId());
            prepStatement.setString(2, author.getFirstName());
            prepStatement.setString(3, author.getLastName());
            prepStatement.setString(4, author.getMiddleName());
            prepStatement.executeUpdate();
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
    public void delete(Long id) {
        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM author WHERE id = ?");
            prepStatement.setLong(1, id);
            prepStatement.executeUpdate();
            logger.info("Author with id = {} was removed", id);
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void update(Author entity) {

    }
}
