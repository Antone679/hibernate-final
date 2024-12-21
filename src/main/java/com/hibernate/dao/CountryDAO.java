package com.hibernate.dao;

import com.hibernate.domain.Country;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class CountryDAO {
    private SessionFactory factory;

    public CountryDAO(SessionFactory factory) {
        this.factory = factory;
    }
    public List<Country> getAll(){
        Query<Country> query = factory.getCurrentSession().createQuery("SELECT " +
                "c from Country c join fetch c.languages", Country.class);

        return query.list();
    }
}
