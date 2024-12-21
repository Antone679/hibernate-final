package com.hibernate.service;

import com.hibernate.dao.CityDAO;
import com.hibernate.domain.City;
import com.hibernate.domain.CountryLanguage;
import com.hibernate.util.ConnectionConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Set;

public class MySQLService {

    private static final SessionFactory sessionFactory;
    private static final CityDAO cityDAO;

    static{
        sessionFactory = ConnectionConfig.getSessionFactory();
        cityDAO = new CityDAO(sessionFactory);
    }

    public static void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                City city = cityDAO.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }
}
