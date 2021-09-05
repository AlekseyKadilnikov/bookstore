package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;

@Singleton
public class BookDAO extends HibernateAbstractDAO<Book, Long> implements IBookDAO {
}
