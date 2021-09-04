package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class RequestRepository implements IRequestRepository {
    private static final Logger logger = LoggerFactory.getLogger(RequestRepository.class);
    private static final String MESSAGE = "Exception";

    @Override
    public List<Request> findAll() {
        List<Request> requests = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Query<Request> query = session.createQuery("FROM Request");
            requests = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }

        return requests;
    }

    @Override
    public Request getById(Long id) {
        Request request = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            request = session.get(Request.class, id);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }

        return request;
    }

    @Override
    public void save(Request request) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.save(request);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Request request) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.update(request);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Request request) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.delete(request);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(MESSAGE, e);
        } finally {
            session.close();
        }
    }
}
