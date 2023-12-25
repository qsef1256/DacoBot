package net.qsef1256.dacobot.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JpaController {

    private final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();
    @Getter
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public JpaController(@NotNull JpaEntityManagerFactory factory) {
        try {
            entityManagerFactory = factory.getEntityManagerFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * EntityManager를 얻습니다.
     *
     * @return entityManager
     */
    public EntityManager getEntityManager() {
        EntityManager entityManager = threadLocal.get();

        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
            threadLocal.set(entityManager);
        }
        return entityManager;
    }

    public void close() {
        EntityManager entityManager = threadLocal.get();
        if (entityManager != null) {
            entityManager.close();
            threadLocal.remove();
        }
    }

    public void shutdown() {
        entityManagerFactory.close();
        threadLocal.remove();
    }

}
