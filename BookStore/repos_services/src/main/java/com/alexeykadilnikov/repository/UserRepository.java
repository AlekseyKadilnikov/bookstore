package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.InjectBean;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.dao.IUserDao;
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

    @InjectBean
    private IUserDao userDao;

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = DBUtils.getConnection().createStatement();

            users = userDao.findAll();

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
            user = userDao.getById(id);

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
        userDao.save(user);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user.getId());
    }

    @Override
    public void saveAll(List<User> all) {
        for(User user : all) {
            userDao.save(user);
        }
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
