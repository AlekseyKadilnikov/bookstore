package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.entity.Request;
import org.springframework.stereotype.Component;

@Component
public class RequestDAO extends HibernateAbstractDAO<Request, Long> implements IRequestDAO {
}
