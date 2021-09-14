package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDAO extends HibernateAbstractDAO<Order, Long> implements IOrderDAO {
}
