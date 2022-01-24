package net.qsef1256.diabot.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.diabot.model.HibernateManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DaoCommonImpl<K extends Serializable, T> implements DaoCommon<K, T> {

    private SessionFactory factory;
    private Class<T> clazz;
    @Getter
    private String clazzName;

    public DaoCommonImpl(final Class<T> clazz) {
        this.clazz = clazz;
        this.clazzName = clazz.getSimpleName();
        this.factory = HibernateManager.getCurrentSessionFromJPA();
    }

    @Override
    public void create(T entity) {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(final T entity) {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.update(entity);
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
    public boolean isExist(K id) {
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

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(clazz);
            criteria.from(clazz);

            List<T> resultList = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            return resultList;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        final Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();

            // TODO: idk this is working? is effective? Nah...
            for (T entity : findAll()) {
                session.delete(entity);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        }
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
            T entity = session.load(clazz, id);
            if (entity == null) {
                throw new NoSuchElementException("Can't find " + clazzName + " with Id: " + id + " to delete");
            }
            session.delete(entity);
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
