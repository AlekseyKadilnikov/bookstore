package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.repository.RequestRepository;
import com.alexeykadilnikov.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BookDao implements IBookDao {
    private static final Logger logger = LoggerFactory.getLogger(BookDao.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try {
            Connection connection = DBUtils.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setPublicationYear(resultSet.getInt("year"));
                book.setCount(resultSet.getInt("count"));
                book.setPrice(resultSet.getInt("price"));
                book.setDescription(resultSet.getString("description"));
                book.setDateOfReceipt(LocalDate.parse(resultSet.getString("date_of_receipt")));
                books.add(book);
            }
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return books;
    }

    @Override
    public Book getById(Long id) {
        Book book = new Book();
        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("SELECT * FROM book WHERE id = ?");
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            book.setId(resultSet.getInt("id"));
            book.setName(resultSet.getString("name"));
            book.setPublisher(resultSet.getString("publisher"));
            book.setPublicationYear(resultSet.getInt("year"));
            book.setCount(resultSet.getInt("count"));
            book.setPrice(resultSet.getInt("price"));
            book.setDescription(resultSet.getString("description"));
            book.setDateOfReceipt(LocalDate.parse(resultSet.getString("date_of_receipt")));

            logger.info("Get book with id = {}", book.getId());

        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return book;
    }

    @Override
    public void save(Book book) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBUtils.getConnection();
            savepoint = connection.setSavepoint();
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement(
                    "INSERT INTO book (id, name, publisher, year, price, count, date_of_receipt, description)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            prepStatement.setLong(1, book.getId());
            prepStatement.setString(2, book.getName());
            prepStatement.setString(3, book.getPublisher());
            prepStatement.setInt(4, book.getPublicationYear());
            prepStatement.setInt(5, book.getPrice());
            prepStatement.setInt(6, book.getCount());
            prepStatement.setDate(7, Date.valueOf(book.getDateOfReceipt()));
            prepStatement.setString(8, book.getDescription());
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
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM book WHERE id = ?");
            prepStatement.setLong(1, id);
            prepStatement.executeUpdate();
            logger.info("Book with id = {} was removed", id);
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void update(Book entity) {
        try {
            Connection connection = DBUtils.getConnection();
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("UPDATE book SET count = ?, price = ? WHERE id = ?");
            prepStatement.setInt(1, entity.getCount());
            prepStatement.setInt(2, entity.getPrice());
            prepStatement.setLong(3, entity.getId());
            prepStatement.executeUpdate();
            connection.commit();
            logger.info("Book with id = {} was updated", entity.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }
}
