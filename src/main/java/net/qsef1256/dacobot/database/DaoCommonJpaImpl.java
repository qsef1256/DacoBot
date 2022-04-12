package net.qsef1256.dacobot.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static net.qsef1256.dacobot.DacoBot.logger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DaoCommonJpaImpl<T, ID extends Serializable> implements DaoCommonJpa<T, ID> {

    @Getter
    private EntityManager entityManager;
    private Class<T> clazz;

    public DaoCommonJpaImpl(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public long count() {
        open();

        AtomicLong count = new AtomicLong();
        execute(entityManager -> count.set(
                (long) entityManager.createQuery("SELECT count(t) FROM " + clazz.getSimpleName() + " t").getSingleResult()));
        return count.get();
    }

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
    public boolean existsById(ID id) {
        initEntityManager();
        return entityManager.contains(findById(id));
    }

    @Override
    public T findById(ID id) {
        initEntityManager();
        return entityManager.find(clazz, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        open();
        Query query = entityManager.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e");
        return (List<T>) query.getResultList();
    }

    @Override
    public void save(T entity) {
        open();
        execute(entityManager -> entityManager.merge(entity));
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

    @Override
    public void delete(T entity) {
        open();
        execute(entityManager -> entityManager.remove(entity));
    }

    @Override
    public void deleteById(ID id) {
        delete(findById(id));
    }

    @Override
    public void deleteAll() {
        open();
        Query query = entityManager.createQuery("DELETE * FROM " + clazz.getSimpleName());
        execute(entityManager -> query.executeUpdate());
    }

    private void initEntityManager() {
        entityManager = JPAManager.getEntityManager();
    }

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
        JPAManager.close();
    }

    @Override
    public void commit() {
        execute(entityManager -> entityManager.getTransaction().commit());
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
            logger.error("Error when execute action: " + this);
            rollback();
            throw e;
        }
    }

    @Override
    public String toString() {
        return "%s@%s [clazz = %s, isOpen = %s, isActive = %s]".formatted(
                getClass().getSimpleName(),
                Integer.toHexString(hashCode()), clazz.getSimpleName(),
                entityManager.isOpen(),
                entityManager.getTransaction().isActive());
    }

}
