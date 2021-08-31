package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BookRepository implements IBookRepository {
    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
            while (resultSet.next()) {
                Book book = new Book();
                setBookFieldsFromResultSet(book, resultSet);
                books.add(book);
            }

            for(Book book : books) {
                resultSet = statement.executeQuery("SELECT * FROM author_book WHERE book_id = " + book.getId());
                List<Long> authorsId = new ArrayList<>();
                while (resultSet.next()) {
                    authorsId.add(resultSet.getLong("author_id"));
                }
                book.setAuthors(authorsId);
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
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("SELECT * FROM ook WHERE id = ?");
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            setBookFieldsFromResultSet(book, resultSet);

            Statement statement = DBUtils.getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM author_book WHERE book_id = " + book.getId());
            List<Long> authorsId = new ArrayList<>();
            while (resultSet.next()) {
                authorsId.add(resultSet.getLong("author_id"));
            }
            book.setAuthors(authorsId);

            logger.info("Get book with id = {} removed", book.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return book;
    }

    @Override
    public void save(Book book) {
        try {
            createAndExecuteQueryForSavingBook(book);
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void delete(Book book) {
        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM book WHERE id = ?");
            prepStatement.setLong(1, book.getId());
            prepStatement.executeUpdate();
            logger.info("Book with id = {} removed", book.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void saveAll(List<Book> all) {
        try {
            for (Book book : all) {
                createAndExecuteQueryForSavingBook(book);
            }
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    public void addRequest(Request request, int count, long id) {
        Book book = getById(id);
        List<Request> commonRequests = book.getCommonRequests();
        Request[] orderRequests = book.getOrderRequests();
        if(request.getStatus() == RequestStatus.COMMON) {
            for(Request r : commonRequests) {
                if(request.getName().equals(r.getName())) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
            commonRequests.add(request);
        }
        else {
            for(Request r : orderRequests) {
                if(request.getName().equals(r.getName()) && request.getStatus() == r.getStatus()) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
        }
    }

    private void setBookFieldsFromResultSet(Book book, ResultSet resultSet) throws SQLException {
        book.setId(resultSet.getInt("id"));
        book.setName(resultSet.getString("name"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setPublicationYear(resultSet.getInt("year"));
        book.setCount(resultSet.getInt("count"));
        book.setPrice(resultSet.getInt("price"));
        book.setDescription(resultSet.getString("description"));
        book.setDateOfReceipt(LocalDate.parse(resultSet.getString("date_of_receipt")));
    }

    private void createAndExecuteQueryForSavingBook(Book book) throws SQLException, IOException {
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

        for(long authorId : book.getAuthors()) {
            prepStatement = DBUtils.getConnection().prepareStatement(
                    "INSERT INTO author_book (author_id, book_id)" +
                            "VALUES (?, ?)");
            prepStatement.setLong(1, authorId);
            prepStatement.setLong(2, book.getId());
            prepStatement.executeUpdate();
        }

        logger.info("Book with id = {} saved", book.getId());
    }
}
