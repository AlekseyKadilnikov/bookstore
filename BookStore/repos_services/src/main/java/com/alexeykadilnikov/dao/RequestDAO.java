package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Request;

@Singleton
public class RequestDAO extends HibernateAbstractDAO<Request, Long> implements IRequestDAO {
}
