package com.hibernate.service;

import com.hibernate.dao.CityDAO;
import com.hibernate.domain.City;
import com.hibernate.util.ConnectionConfig;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class CityService {

    private CityDAO cityDAO;

    public CityService(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }

    public List<City> fetchData(){
        List<City> allCities = new ArrayList<>();
        try(Session session = ConnectionConfig.getSessionFactory().getCurrentSession()){

            session.beginTransaction();
            int totalCount = cityDAO.getTotalCount();
            int step = 500;

            for (int i = 0; i < totalCount; i += step) {
            allCities.addAll(cityDAO.getItems(i, step));

            }
            session.getTransaction().commit();
        }
        return allCities;
    }
}
