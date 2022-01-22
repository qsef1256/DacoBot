package net.qsef1256.diabot.model;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateManager {
    @Getter
    private static EntityManagerFactory entityManagerFactory;

    private HibernateManager() {
    }

    public static SessionFactory getCurrentSessionFromJPA() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        return session.getSessionFactory();
    }

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("net.qsef1256.diabot");
        } catch (final Exception e) {
            entityManagerFactory.close();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void shutdown() {
        entityManagerFactory.close();
    }

}
