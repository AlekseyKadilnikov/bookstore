package com.alexeykadilnikov.dao;

import com.alexeykadilnikov.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class HibernateAbstractDAO <T, PK extends Serializable> {
    private static final Logger logger = LoggerFactory.getLogger(HibernateAbstractDAO.class);
    private static final String MESSAGE = "Exception";

    private Class<T> type;
    private SessionFactory sessionFactory;

    protected HibernateAbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public T getById(PK id){
        T t = null;

        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            t = session.get(type, id);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }

        return t;
    }

    public List<T> findAll() {
        List<T> entityList = new ArrayList<>();

        Session session = getCurrentSession();

        try {
            session.beginTransaction();
            entityList = session.createQuery("from " + type.getName()).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return entityList;
    }

    public void save(T entity) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            session.saveOrUpdate(entity);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    public void update(T entity) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            session.update(entity);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    public void delete(T entity) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            session.delete(entity);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    protected Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}
