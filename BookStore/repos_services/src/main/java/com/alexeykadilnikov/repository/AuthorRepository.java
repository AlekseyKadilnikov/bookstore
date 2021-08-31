package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.utils.DBUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class AuthorRepository implements IAuthorRepository {
    private static final Logger logger = LoggerFactory.getLogger(AuthorRepository.class);
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
                setAuthorFieldsFromResultSet(author, resultSet);
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
            setAuthorFieldsFromResultSet(author, resultSet);

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
        try {
            createAndExecuteQueryForSavingAuthor(author);
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void delete(Author author) {
        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM author WHERE id = ?");
            prepStatement.setLong(1, author.getId());
            prepStatement.executeUpdate();
            logger.info("Author with id = {} was removed", author.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void saveAll(List<Author> all) {
        try {
            for(Author author : all) {
                createAndExecuteQueryForSavingAuthor(author);
            }
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    private void setAuthorFieldsFromResultSet(Author author, ResultSet resultSet) throws SQLException {
        author.setId(resultSet.getInt("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        author.setMiddleName(resultSet.getString("middle_name"));
    }

    private void createAndExecuteQueryForSavingAuthor(Author author) throws SQLException, IOException {
        PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement(
                "INSERT INTO author (id, first_name, last_name, middle_name)" +
                        "VALUES (?, ?, ?, ?)");
        prepStatement.setLong(1, author.getId());
        prepStatement.setString(2, author.getFirstName());
        prepStatement.setString(3, author.getLastName());
        prepStatement.setString(4, author.getMiddleName());
        prepStatement.executeUpdate();

        logger.info("Author with id = {} saved", author.getId());
    }
}
