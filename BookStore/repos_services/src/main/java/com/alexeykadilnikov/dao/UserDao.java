package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.Singleton;
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
import java.util.List;

@Singleton
public class UserDao implements IUserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
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
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("name"));
                users.add(user);
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
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("name"));

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
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement(
                    "INSERT INTO user (name) VALUES (?)");
            prepStatement.setString(1, user.getUsername());
            prepStatement.executeUpdate();
            DBUtils.getConnection().commit();
            logger.info("User {} was saved", user.getUsername());
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            PreparedStatement prepStatement = DBUtils.getConnection().prepareStatement("DELETE FROM author WHERE id = ?");
            prepStatement.setLong(1, id);
            prepStatement.executeUpdate();
            logger.info("User with id = {} was removed", id);
        } catch (SQLException e) {
            logger.error(SQL_EX_MESSAGE, e);
        } catch (IOException e) {
            logger.error(IO_EX_MESSAGE, e);
        }
    }

    @Override
    public void update(User entity) {

    }
}
