package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Author;

@Singleton
public class AuthorDAO extends HibernateAbstractDAO<Author, Long> {
}
