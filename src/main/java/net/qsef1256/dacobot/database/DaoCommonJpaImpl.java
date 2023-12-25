package net.qsef1256.dacobot.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DaoCommonJpaImpl<T, K extends Serializable> implements DaoCommonJpa<T, K> {

    private JpaController jpaController;
    @Getter
    private EntityManager entityManager;
    private Class<T> clazz;

    public DaoCommonJpaImpl(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    // FIXME: DI 방식으로 생성되지 않은 경우 Spring IoC 범위 밖이라 jpaController 를 AutoWired 할 수 없는 것으로 추정됨
    @Autowired
    public void setJpaController(@NotNull JpaController jpaController) {
        this.jpaController = jpaController;
    }
    
    @Override
    public long count() {
        open();

        AtomicLong count = new AtomicLong();
        execute(manager -> count.set((long) manager
                .createQuery("SELECT count(t) FROM " + clazz.getSimpleName() + " t")
                .getSingleResult()));

        return count.get();
    }

    /**
     * @param constraint constraint Map
     * @return list of entity that matches constraint
     */
    @Override
    public List<T> findBy(@NotNull Map<String, Object> constraint) {
        open();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> root = criteria.from(clazz);
        constraint.forEach((key, value) -> criteria.where(builder.equal(root.get(key), value)));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public boolean existsById(K id) {
        initEntityManager();
        return entityManager.contains(findById(id));
    }

    @Override
    public T findById(K id) {
        initEntityManager();
        return entityManager.find(clazz, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        open();
        Query query = entityManager.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e");
        return query.getResultList();
    }

    @Override
    public void save(T entity) {
        open();
        execute(manager -> manager.merge(entity));
    }

    @Override
    public void saveAndClose(T entity) {
        save(entity);
        close();
    }

    @Override
    public void saveAll(@NotNull Iterable<T> entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    @Override
    public void saveAllAndClose(@NotNull Iterable<T> entities) {
        for (T entity : entities) {
            saveAndClose(entity);
        }
    }

    /**
     * 엔티티를 삭제합니다. 엔티티는 관리되고 있어야 합니다.
     *
     * @param entity managed Entity
     * @throws IllegalArgumentException when entity is detached
     */
    @Override
    public void delete(T entity) {
        open();
        execute(manager -> manager.remove(entity));
    }

    @Override
    public void deleteById(K id) {
        delete(findById(id));
    }

    @Override
    public void deleteAll() {
        open();
        Query query = entityManager.createQuery("DELETE FROM " + clazz.getSimpleName());
        execute(manager -> query.executeUpdate());
    }

    private void initEntityManager() {
        entityManager = jpaController.getEntityManager();
    }

    /**
     * 트랜잭션을 엽니다.
     * <p>트랜잭션이 필요한 작업은 명시적인 open() 호출이 없어도 open() 을 자동으로 호출 합니다.</p>
     */
    @Override
    public void open() {
        initEntityManager();

        if (!entityManager.getTransaction().isActive()) begin();
    }

    private void begin() {
        entityManager.getTransaction().begin();
    }

    @Override
    public void close() {
        commit();
        jpaController.close();
    }

    @Override
    public void commit() {
        execute(manager -> manager.getTransaction().commit());
    }

    @Override
    public void rollback() {
        entityManager.getTransaction().rollback();
    }

    @Override
    public Query createQuery(String s) {
        return entityManager.createQuery(s);
    }

    private void execute(Consumer<EntityManager> action) {
        try {
            action.accept(entityManager);
        } catch (RuntimeException e) {
            log.error("Error when execute action: %s".formatted(this));
            rollback();
            throw e;
        }
    }

    @Override
    public String toString() {
        String toString = "%s [clazz = %s".formatted(super.toString(), clazz.getSimpleName());

        if (entityManager == null) {
            return toString + ", entityManager = null]"; // is call open() before transaction?
        } else {
            return toString + ", isOpen = %s, isActive = %s]".formatted(
                    entityManager.isOpen(),
                    entityManager.getTransaction().isActive());
        }
    }

}
