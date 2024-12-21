package com.hibernate.util;

import com.hibernate.domain.City;
import com.hibernate.domain.Country;
import com.hibernate.domain.CountryLanguage;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

import static java.util.Objects.nonNull;

public class ConnectionConfig {

    private static SessionFactory sessionFactory;
    public static RedisClient redisClient;

    static {
        try {
            Properties properties = new Properties();
            properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
            properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            properties.put(Environment.URL, "jdbc:mysql://localhost:3306/world");
            properties.put(Environment.USER, "root");
            properties.put(Environment.PASS, "root");
            properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            properties.put(Environment.HBM2DDL_AUTO, "validate");
            properties.put(Environment.STATEMENT_BATCH_SIZE, "100");

            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(City.class);
            configuration.addAnnotatedClass(Country.class);
            configuration.addAnnotatedClass(CountryLanguage.class);
            configuration.addProperties(properties);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            redisClient = prepareRedisClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        ConnectionConfig.sessionFactory = sessionFactory;
    }

    public static RedisClient getRedisClient() {
        return redisClient;
    }

    public static void setRedisClient(RedisClient redisClient) {
        ConnectionConfig.redisClient = redisClient;
    }

    public static RedisClient prepareRedisClient() {
        RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            System.out.println("\nConnected to Redis\n");
        }
        return redisClient;
    }

    public static void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }
}
