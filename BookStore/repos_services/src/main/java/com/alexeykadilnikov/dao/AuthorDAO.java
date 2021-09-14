package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorDAO extends HibernateAbstractDAO<Author, Long> implements IAuthorDAO {
}
