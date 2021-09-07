package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RequestDao implements IRequestDao {
    private static final Logger logger = LoggerFactory.getLogger(RequestDao.class);
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
                request.setId(resultSet.getLong("id"));
                request.setName(resultSet.getString("name"));
                request.setStatus(RequestStatus.values()[resultSet.getInt("status_code")]);
                request.setCount(resultSet.getInt("count"));
                requests.add(request);
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
            request.setId(resultSet.getLong("id"));
            request.setName(resultSet.getString("name"));
            request.setStatus(RequestStatus.values()[resultSet.getInt("status_code")]);
            request.setCount(resultSet.getInt("count"));

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
            PreparedStatement prepStatement = connection.prepareStatement(
                    "INSERT INTO request (name, status_code, count)" +
                            "VALUES (?, ?, ?)");
            prepStatement.setString(1, request.getName());
            prepStatement.setInt(2, request.getStatus().getStatusCode());
            prepStatement.setInt(3, request.getCount());
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
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM request WHERE id = ?");
            prepStatement.setLong(1, id);
            prepStatement.executeUpdate();
            logger.info("Request with id = {} was removed", id);
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void update(Request request) {
        try {
            Connection connection = DBUtils.getConnection();
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("UPDATE request SET count = ? WHERE id = ?");
            prepStatement.setInt(1, request.getCount());
            prepStatement.setLong(2, request.getId());
            prepStatement.executeUpdate();
            connection.commit();
            logger.info("Request with id = {} was updated", request.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }
}
