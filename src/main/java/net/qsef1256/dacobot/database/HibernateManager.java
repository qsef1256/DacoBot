package net.qsef1256.dacobot.database;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static net.qsef1256.dacobot.DacoBot.logger;

public class HibernateManager {
    @Getter
    private static EntityManagerFactory entityManagerFactory;

    private HibernateManager() {
    }

    public static SessionFactory getSessionFactoryFromJPA() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        session.getSessionFactory().openSession(); // 원래 쓰고 닫아야 하지만 DaoCommon 은 getCurrentSession() 을 사용하므로 당장은 필요치 않음
        return session.getSessionFactory();
    }

    // 쓰고 닫을것 (Non-thread-safe)
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("net.qsef1256.dacobot");
        } catch (final Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            entityManagerFactory.close();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void shutdown() {
        entityManagerFactory.close();
    }

}
