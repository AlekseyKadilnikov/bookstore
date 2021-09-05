package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Order;

@Singleton
public class OrderDAO extends HibernateAbstractDAO<Order, Long> {
}
