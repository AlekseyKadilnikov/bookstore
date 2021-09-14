package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.User;

@Singleton
public class UserDAO extends HibernateAbstractDAO<User, Long> implements IUserDAO {
}
