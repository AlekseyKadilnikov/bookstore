package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Singleton
public class OrderRepository implements IOrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM order_t");
            while (resultSet.next()) {
                Order order = new Order();
                setOrderFieldsFromResultSet(order, resultSet);
                orders.add(order);
            }

            for(Order order : orders) {
                setBooksForOrder(order, statement);
            }
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return orders;
    }

    @Override
    public Order getById(Long id) {
        Order order = new Order();

        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("SELECT * FROM order_t WHERE id = ?");
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            setOrderFieldsFromResultSet(order, resultSet);

            Statement statement = DBUtils.getConnection().createStatement();
            setBooksForOrder(order, statement);

            logger.info("Get order with id = {}", order.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return order;
    }

    @Override
    public void save(Order order) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBUtils.getConnection();
            savepoint = connection.setSavepoint();
            createAndExecuteQueryForSavingOrder(order);
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
    public void delete(Order order) {
        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM order_t WHERE id = ?");
            prepStatement.setLong(1, order.getId());
            prepStatement.executeUpdate();
            logger.info("Order with id = {} was removed", order.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void saveAll(List<Order> all) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = DBUtils.getConnection();
            savepoint = connection.setSavepoint();
            for(Order order : all) {
                createAndExecuteQueryForSavingOrder(order);
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
    public void update(Order order) {
        try {
            Connection connection = DBUtils.getConnection();
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("UPDATE order_t SET status_code = ?, exec_date = ? WHERE id = ?");
            prepStatement.setInt(1, order.getStatus().getStatusCode());
            prepStatement.setTimestamp(2, Timestamp.valueOf(order.getExecutionDate()));
            prepStatement.setLong(3, order.getId());
            prepStatement.executeUpdate();
            connection.commit();
            logger.info("Order with id = {} was updated", order.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    private void setOrderFieldsFromResultSet(Order order, ResultSet resultSet) throws SQLException {
        order.setId(resultSet.getInt("id"));
        order.setUserId(resultSet.getLong("user_id"));
        order.setTotalPrice(resultSet.getInt("total_price"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.setInitDate(LocalDateTime.parse(resultSet.getString("init_date"), dtf));
        if(resultSet.getString("exec_date") != null) {
            order.setExecutionDate(LocalDateTime.parse(resultSet.getString("exec_date"), dtf));
        }
        order.setStatus(OrderStatus.values()[resultSet.getInt("status_code")]);
    }

    private void createAndExecuteQueryForSavingOrder(Order order) throws SQLException, IOException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement prepStatement = conn.prepareStatement(
                "INSERT INTO order_t (user_id, total_price, init_date, exec_date, status_code)" +
                        "VALUES (?, ?, ?, ?, ?)");
        prepStatement.setLong(1, order.getUserId());
        prepStatement.setInt(2, order.getTotalPrice());
        prepStatement.setTimestamp(3, Timestamp.valueOf(order.getInitDate()));
        if(order.getExecutionDate() != null) {
            prepStatement.setTimestamp(4, Timestamp.valueOf(order.getExecutionDate()));
        } else {
            prepStatement.setNull(4, Types.DATE);
        }
        prepStatement.setInt(5, order.getStatus().getStatusCode());
        prepStatement.executeUpdate();

        prepStatement = conn.prepareStatement("SELECT LAST_INSERT_ID()");
        ResultSet resultSet = prepStatement.executeQuery();
        resultSet.next();
        long lastInsertedOrderId = resultSet.getLong(1);

        prepStatement = conn.prepareStatement(
                "INSERT INTO order_book (order_id, book_id, book_count)" +
                        "VALUES (?, ?, ?)");
        for(Map.Entry<Long, Integer> entry : order.getBooks().entrySet()) {
            prepStatement.setLong(1, lastInsertedOrderId);
            prepStatement.setLong(2, entry.getKey());
            prepStatement.setInt(3, entry.getValue());
            prepStatement.executeUpdate();
        }

        order.setId(lastInsertedOrderId);
        conn.commit();
        logger.info("Order with id = {} was saved", lastInsertedOrderId);
    }

    private void setBooksForOrder(Order order, Statement statement) throws SQLException {
        ResultSet resultSetBook = statement.executeQuery("SELECT b.id, o.book_count FROM book AS b " +
                "JOIN order_book AS o ON b.id = o.book_id WHERE o.order_id = " + order.getId());
        Map<Long, Integer> books = new HashMap<>();
        while (resultSetBook.next()) {
            long bookId = resultSetBook.getLong("id");
            books.put(bookId, resultSetBook.getInt("book_count"));
        }
        order.setBooks(books);
    }
}
