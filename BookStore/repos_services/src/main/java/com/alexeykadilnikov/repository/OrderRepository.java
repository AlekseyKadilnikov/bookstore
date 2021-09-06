package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.dao.IOrderDao;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Singleton
public class OrderRepository implements IOrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @InjectBean
    private IOrderDao orderDao;

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();

            orders = orderDao.findAll();

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
            order = orderDao.getById(id);

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
        orderDao.delete(order.getId());
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
        orderDao.update(order);
    }

    private void createAndExecuteQueryForSavingOrder(Order order) throws SQLException, IOException {
        Connection conn = DBUtils.getConnection();

        orderDao.save(order);

        PreparedStatement prepStatement = conn.prepareStatement("SELECT LAST_INSERT_ID()");
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
