package net.qsef1256.dacobot.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.dacobot.setting.DiaSetting;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

import static net.qsef1256.dacobot.DacoBot.logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JPAManager {

    private static final ThreadLocal<EntityManager> threadLocal;
    @Getter
    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            Properties properties = DiaSetting.getSetting();

            entityManagerFactory = Persistence.createEntityManagerFactory(properties.getProperty("main.package"));
            threadLocal = new ThreadLocal<>();
        } catch (final Exception e) {
            logger.error(e.getMessage());
            entityManagerFactory.close();
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
            threadLocal.set(null);
        }
    }

    @Deprecated
    public static SessionFactory getSessionFactoryFromJPA() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        session.getSessionFactory().openSession(); // 원래 쓰고 닫아야 하지만 DaoCommonHibernateImpl 은 getCurrentSession() 을 사용하므로 당장은 필요치 않음
        return session.getSessionFactory();
    }

    public static void shutdown() {
        entityManagerFactory.close();
    }

}
