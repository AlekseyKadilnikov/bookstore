package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserRepository implements IUserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private static final String MESSAGE = "Exception";

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Query<User> query = session.createQuery("FROM User");
            users = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }

        return users;
    }

    @Override
    public User getById(Long id) {
        User user = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            user = session.get(User.class, id);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }

        return user;
    }

    @Override
    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.save(user);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    @Override
    public void update(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.update(user);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.delete(user);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }
}
