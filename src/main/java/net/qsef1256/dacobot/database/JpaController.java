package net.qsef1256.dacobot.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static net.qsef1256.dacobot.DacoBot.logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaController {

    private static final ThreadLocal<EntityManager> threadLocal;
    @Getter
    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = new JpaEntityManagerFactory().getEntityManagerFactory();
            threadLocal = new ThreadLocal<>();
        } catch (Exception e) {
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

    public static void shutdown() {
        entityManagerFactory.close();
        threadLocal.remove();
    }

}
