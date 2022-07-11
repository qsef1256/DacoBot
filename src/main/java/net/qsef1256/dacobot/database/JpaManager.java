package net.qsef1256.dacobot.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import static net.qsef1256.dacobot.DacoBot.logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaManager {

    private static final ThreadLocal<EntityManager> threadLocal;
    @Getter
    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = new JpaEntityManagerFactory().getEntityManagerFactory();
            threadLocal = new ThreadLocal<>();
        } catch (final Exception e) {
            logger.error(e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * EntityManager를 얻습니다.
     *
     * @return entityManager
     */
    public static EntityManager getEntityManager() {
        EntityManager entityManager = threadLocal.get();

        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
            threadLocal.set(entityManager);
        }
        return entityManager;
    }

    public static void close() {
        EntityManager entityManager = threadLocal.get();
        if (entityManager != null) {
            entityManager.close();
            threadLocal.remove();
        }
    }

    /**
     * @return Hibernate SessionFactory
     * @deprecated use JPA EntityManager instead
     */
    @Deprecated
    public static SessionFactory getSessionFactoryFromJPA() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        session.getSessionFactory().openSession(); // 원래 쓰고 닫아야 하지만 DaoCommonHibernateImpl 은 getCurrentSession() 을 사용하므로 당장은 필요치 않음
        return session.getSessionFactory();
    }

    public static void shutdown() {
        entityManagerFactory.close();
        threadLocal.remove();
    }

}
