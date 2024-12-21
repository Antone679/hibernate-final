package com.hibernate.dao;

import com.hibernate.domain.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CityDAO {
    private SessionFactory factory;

    public CityDAO(SessionFactory factory) {
        this.factory = factory;
    }

    public City getById(Integer id) {
        Query<City> query = factory.getCurrentSession().createQuery("SELECT " +
                "c from City c join fetch c.country WHERE c.id = :ID", City.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }

    public List<City> getItems(int offset, int limit) {
        List<City> cities = null;
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            Query<City> query = session.createQuery("SELECT c FROM City c", City.class);
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            cities = query.list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return cities;
    }

    public int getTotalCount() {
        Query<Long> query = factory.getCurrentSession().createQuery("SELECT " +
                " count(c) from City c", Long.class);

        return Math.toIntExact(query.getSingleResult());
    }
}
