package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.dao.IRequestDao;
import com.alexeykadilnikov.entity.Request;
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
public class RequestRepository implements IRequestRepository {
    private static final Logger logger = LoggerFactory.getLogger(RequestRepository.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @InjectBean
    private IRequestDao requestDao;

    @Override
    public List<Request> findAll() {
        List<Request> requests = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();
            requests = requestDao.findAll();

            for(Request request : requests) {
                setBooksAndOrdersForRequest(request, statement);
            }
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return requests;
    }

    @Override
    public Request getById(Long id) {
        Request request = new Request();

        try {
            request = requestDao.getById(id);

            Statement statement = DBUtils.getConnection().createStatement();
            setBooksAndOrdersForRequest(request, statement);

            logger.info("Get request with id = {}", request.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return request;
    }

    @Override
    public void save(Request request) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBUtils.getConnection();
            savepoint = connection.setSavepoint();
            createAndExecuteQueryForSavingRequest(request);
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
    public void delete(Request request) {
        requestDao.delete(request.getId());
    }

    @Override
    public void update(Request request) {
        requestDao.update(request);
    }

    @Override
    public void saveAll(List<Request> all) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBUtils.getConnection();
            savepoint = connection.setSavepoint();
            for(Request request : all) {
                createAndExecuteQueryForSavingRequest(request);
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

    private void createAndExecuteQueryForSavingRequest(Request request) throws SQLException, IOException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement prepStatement = conn.prepareStatement("SELECT id FROM request WHERE name = ? AND status_code = ?");
        prepStatement.setString(1, request.getName());
        prepStatement.setInt(2, request.getStatus().getStatusCode());
        ResultSet resultSet = prepStatement.executeQuery();
        if(resultSet.next()) {
            prepStatement = conn.prepareStatement("UPDATE request SET count = count + ? WHERE id = ?");
            prepStatement.setInt(1, request.getCount());
            prepStatement.setLong(2, resultSet.getLong("id"));
            prepStatement.executeUpdate();
            logger.info("Request count with name = \"{}\" was incremented", request.getName());
            DBUtils.getConnection().commit();
            return;
        }

        requestDao.save(request);

        prepStatement = conn.prepareStatement("SELECT LAST_INSERT_ID()");
        resultSet = prepStatement.executeQuery();
        resultSet.next();
        long lastInsertedRequestId = resultSet.getLong(1);

        prepStatement = conn.prepareStatement(
                "INSERT INTO book_request (book_id, request_id)" +
                        "VALUES (?, ?)");
        for(Long bookId : request.getBooksId()) {
            prepStatement.setLong(1, bookId);
            prepStatement.setLong(2, lastInsertedRequestId);
            prepStatement.executeUpdate();
        }

        request.setId(lastInsertedRequestId);
        logger.info("Request with id = {} was saved", lastInsertedRequestId);
        DBUtils.getConnection().commit();
    }

    private void setBooksAndOrdersForRequest(Request request, Statement statement) throws SQLException {
        ResultSet resultSetBook = statement.executeQuery("SELECT * FROM book_request WHERE request_id = " + request.getId());
        Set<Long> booksId = new HashSet<>();
        while (resultSetBook.next()) {
            booksId.add(resultSetBook.getLong("book_id"));
        }
        request.setBooksId(booksId);
    }
}
