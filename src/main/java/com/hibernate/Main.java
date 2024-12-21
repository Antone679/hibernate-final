package com.hibernate;

import com.hibernate.dao.CityDAO;
import com.hibernate.dao.CountryDAO;
import com.hibernate.domain.City;
import com.hibernate.redis.CityCountry;
import com.hibernate.service.CityService;
import com.hibernate.service.MySQLService;
import com.hibernate.service.RedisService;
import com.hibernate.util.ConnectionConfig;
import io.lettuce.core.RedisClient;
import org.hibernate.SessionFactory;

import java.util.List;

import static com.hibernate.service.CityCountryService.transformData;

public class Main {

    public static final SessionFactory sessionFactory;
    public static final RedisClient redisClient;
    public static final CityDAO cityDAO;
    public static final CountryDAO countryDAO;

    static {
        sessionFactory = ConnectionConfig.getSessionFactory();
        redisClient = ConnectionConfig.getRedisClient();
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
    }

    public static void main(String[] args) {

        CityService cityService = new CityService(cityDAO);
        List<City> allCities = cityService.fetchData();
        List<CityCountry> preparedData = transformData(allCities);

        RedisService.pushToRedis(preparedData);
        sessionFactory.getCurrentSession().close();

        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        long startRedis = System.currentTimeMillis();
        RedisService.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        MySQLService.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        ConnectionConfig.shutdown();
    }
}