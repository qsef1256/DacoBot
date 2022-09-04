package net.qsef1256.dacobot.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Deprecated(since = "DaoCommonOld")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DaoCommonHibernateImpl<K extends Serializable, T> implements DaoCommonOld<T, K> {

    private SessionFactory factory;
    private Class<T> clazz;
    @Getter
    private String clazzName;

    public DaoCommonHibernateImpl(final @NotNull Class<T> clazz) {
        this.clazz = clazz;
        this.clazzName = clazz.getSimpleName();
        this.factory = JpaManager.getSessionFactoryFromJPA();
    }

    public Session getCurrentSession() {
        return factory.getCurrentSession();
    }

    @Override
    public long count() {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Long> criteria = builder.createQuery(Long.class);
            criteria.select(builder.count(criteria.from(clazz)));

            Long result = session.createQuery(criteria).getSingleResult();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(T entity) {
        create(List.of(entity));
    }

    @Override
    public void create(List<T> entities) {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            for (T entity : entities) {
                session.persist(entity);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(final T entity) {
        update(List.of(entity));
    }

    @Override
    public void update(List<T> entities) {
        saveAll(entities);
    }

    @Override
    public void save(final T entity) {
        saveAll(List.of(entity));
    }

    @Override
    public void saveAll(Iterable<T> entities) {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            for (T entity : entities) {
                session.merge(entity);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public T findById(K id) {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            final T entity = session.get(clazz, id);
            session.getTransaction().commit();
            if (entity == null)
                throw new NoSuchElementException("Element " + clazzName + " with id " + id + " is not exist on database.");
            return entity;
        } catch (NoSuchElementException e) {
            session.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findBy(Map<String, Object> constraint) {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<T> criteria = builder.createQuery(clazz);
            JpaRoot<T> root = criteria.from(clazz);
            constraint.forEach((key, value) -> criteria.where(builder.equal(root.get(key), value)));

            List<T> result = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            return result;
        } catch (NoSuchElementException e) {
            session.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(K id) {
        try {
            final T entity = findById(id);
            return entity != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public List<T> findAll() {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();

            List<T> resultList = getAllList(session);
            session.getTransaction().commit();
            return resultList;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    private List<T> getAllList(@NotNull Session session) {
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);

        return session.createQuery(criteria).getResultList();
    }

    @Override
    public void deleteAll() {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();

            for (T entity : getAllList(session)) { // TODO: this is not effective
                session.remove(entity);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    // This is fast delete all item, but not working when entity is cascaded
    public int hqlTruncate(String myTable) {
        final Session session = factory.getCurrentSession();
        String hql = String.format("delete from %s", myTable);
        Query query = session.createQuery(hql);
        return query.executeUpdate();
    }

    @Override
    public void delete(final T entity) {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(K id) {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            T entity = session.getReference(clazz, id);
            if (entity == null) {
                throw new NoSuchElementException("Can't find " + clazzName + " with Id: " + id + " to delete");
            }
            session.remove(entity);
            session.getTransaction().commit();
        } catch (NoSuchElementException e) {
            session.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
}
