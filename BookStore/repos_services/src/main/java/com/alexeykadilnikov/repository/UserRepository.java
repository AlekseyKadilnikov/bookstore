package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class UserRepository implements IUserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private static final String SQL_EX_MESSAGE = "SQL Exception";
    private static final String IO_EX_MESSAGE = "IO Exception";

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                User user = new User();
                setUserFieldsFromResultSet(user, resultSet);
                users.add(user);
            }

            for(User user : users) {
                setOrdersForUser(user, statement);
            }

        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return users;
    }

    @Override
    public User getById(Long id) {
        User user = new User();

        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("SELECT * FROM author WHERE id = ?");
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            setUserFieldsFromResultSet(user, resultSet);

            Statement statement = DBUtils.getConnection().createStatement();
            setOrdersForUser(user, statement);

            logger.info("Get author with id = {}", user.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
        return user;
    }

    @Override
    public void save(User user) {
        try {
            createAndExecuteQueryForSavingUser(user);
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void delete(User user) {
        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM author WHERE id = ?");
            prepStatement.setLong(1, user.getId());
            prepStatement.executeUpdate();
            logger.info("User with id = {} was removed", user.getId());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void saveAll(List<User> all) {
        try {
            for(User user : all) {
                createAndExecuteQueryForSavingUser(user);
            }
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    private void setUserFieldsFromResultSet(User user, ResultSet resultSet) throws SQLException {
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("name"));
    }

    private void createAndExecuteQueryForSavingUser(User user) throws SQLException, IOException {
        PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement(
                "INSERT INTO user (name) VALUES (?)");
        prepStatement.setString(1, user.getUsername());
        prepStatement.executeUpdate();
        logger.info("User {} was saved", user.getUsername());
        DBUtils.getConnection().commit();
    }

    private void setOrdersForUser(User user, Statement statement) throws SQLException, IOException {
        ResultSet resultSetOrder = statement.executeQuery("SELECT * FROM order_t WHERE user_id = " + user.getId());
        Set<Long> ordersId = new HashSet<>();
        while (resultSetOrder.next()) {
            ordersId.add(resultSetOrder.getLong("id"));
        }
        user.setOrders(ordersId);
    }
}
