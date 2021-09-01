package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.Singleton;
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

    @Override
    public List<Request> findAll() {
        List<Request> requests = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM request");
            while (resultSet.next()) {
                Request request = new Request();
                setRequestFieldsFromResultSet(request, resultSet);
                requests.add(request);
            }

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
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("SELECT * FROM request WHERE id = ?");
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            setRequestFieldsFromResultSet(request, resultSet);

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
        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM request WHERE id = ?");
            prepStatement.setLong(1, request.getId());
            prepStatement.executeUpdate();
            logger.info("Request with id = {} was removed", request.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
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

    private void setRequestFieldsFromResultSet(Request request, ResultSet resultSet) throws SQLException {
        request.setId(resultSet.getLong("id"));
        request.setName(resultSet.getString("name"));
        request.setStatus(RequestStatus.values()[resultSet.getInt("status_code")]);
        request.setCount(resultSet.getInt("count"));
    }

    private void createAndExecuteQueryForSavingRequest(Request request) throws SQLException, IOException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement prepStatement = conn.prepareStatement(
                "INSERT INTO request (name, status_code, count)" +
                        "VALUES (?, ?, ?)");
        prepStatement.setString(1, request.getName());
        prepStatement.setInt(2, request.getStatus().getStatusCode());
        prepStatement.setInt(3, request.getCount());
        prepStatement.executeUpdate();

        prepStatement = conn.prepareStatement("SELECT LAST_INSERT_ID()");
        ResultSet resultSet = prepStatement.executeQuery();
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

        prepStatement = conn.prepareStatement(
                "INSERT INTO order_request (order_id, request_id)" +
                        "VALUES (?, ?)");
        for(Long orderId : request.getOrdersId()) {
            prepStatement.setLong(1, orderId);
            prepStatement.setLong(2, lastInsertedRequestId);
            prepStatement.executeUpdate();
        }

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

        ResultSet resultSetOrder = statement.executeQuery("SELECT * FROM order_request WHERE request_id = " + request.getId());
        Set<Long> ordersId = new HashSet<>();
        while (resultSetOrder.next()) {
            ordersId.add(resultSetOrder.getLong("order_id"));
        }
        request.setOrdersId(ordersId);
    }
}
