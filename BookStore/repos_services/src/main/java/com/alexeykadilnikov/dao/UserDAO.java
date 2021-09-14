package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDAO extends HibernateAbstractDAO<User, Long> implements IUserDAO {
}
