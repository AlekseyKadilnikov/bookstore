package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookDAO extends HibernateAbstractDAO<Book, Long> implements IBookDAO {
}
