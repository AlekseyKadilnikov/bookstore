package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.dao.IBookDao;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.utils.DBUtils;
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
public class BookRepository implements IBookRepository {
    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @InjectBean
    private IBookDao bookDao;

    @Override
    public List<Book> findAll() {
        List<Book> books = bookDao.findAll();
        try {
            Connection connection = DBUtils.getConnection();
            Statement statement = connection.createStatement();
            for(Book book : books) {
                setRequestsAndAuthorsForBook(statement, book);
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
            book = bookDao.getById(id);

            Statement statement = DBUtils.getConnection().createStatement();
            setRequestsAndAuthorsForBook(statement, book);

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
            createAndExecuteQueryForSavingBook(book);
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
    public void update(Book book) {
        bookDao.update(book);
    }

    @Override
    public void delete(Book book) {
        bookDao.delete(book.getId());
    }

    @Override
    public void saveAll(List<Book> all) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBUtils.getConnection();
            savepoint = connection.setSavepoint();
            for (Book book : all) {
                createAndExecuteQueryForSavingBook(book);
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

    @Override
    public void addRequest(Request request, int count, Book book) {
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
                if(r == null) {
                    if(request.getStatus() == RequestStatus.NEW) {
                        orderRequests[0] = request;
                    } else {
                        orderRequests[1] = request;
                    }
                } else if(request.getName().equals(r.getName()) && request.getStatus() == r.getStatus()) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
        }
    }

    private void createAndExecuteQueryForSavingBook(Book book) throws SQLException, IOException {
        bookDao.save(book);

        PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement(
                "INSERT INTO author_book (author_id, book_id)" +
                        "VALUES (?, ?)");
        for(long authorId : book.getAuthors()) {
            prepStatement.setLong(1, authorId);
            prepStatement.setLong(2, book.getId());
            prepStatement.executeUpdate();
        }
        DBUtils.getConnection().commit();
        logger.info("Book with id = {} was saved", book.getId());
    }

    private void setRequestsAndAuthorsForBook(Statement statement, Book book) throws SQLException, IOException {
        ResultSet resultSetAuthor = statement.executeQuery("SELECT * FROM author_book WHERE book_id = " + book.getId());
        Set<Long> authorsId = new HashSet<>();
        while (resultSetAuthor.next()) {
            authorsId.add(resultSetAuthor.getLong("author_id"));
        }
        book.setAuthors(authorsId);

        ResultSet resultSetRequest = statement.executeQuery("SELECT * FROM request AS r " +
                "JOIN book_request AS b ON b.request_id = r.id WHERE b.book_id = " + book.getId());
        while (resultSetRequest.next()) {
            Request request = new Request();
            request.setId(resultSetRequest.getLong("id"));
            request.setName(resultSetRequest.getString("name"));
            request.setStatus(RequestStatus.values()[resultSetRequest.getInt("status_code")]);
            request.setCount(resultSetRequest.getInt("count"));

            addRequest(request, request.getCount(), book);
        }
    }
}
