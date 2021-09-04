package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class BookRepository implements IBookRepository {
    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);
    private static final String MESSAGE = "Exception";

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Query<Book> query = session.createQuery("FROM Book");
            books = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }

        return books;
    }

    @Override
    public Book getById(Long id) {
        Book book = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            book = session.get(Book.class, id);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }

        return book;
    }

    @Override
    public void save(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.save(book);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.update(book);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.delete(book);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }
}
