package net.qsef1256.diabot.database;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface DaoCommon<K extends Serializable, T> {

    Session getCurrentSession();

    void create(T entity);

    void create(List<T> entity);

    void update(T entity);

    void update(List<T> entity);

    List<T> findBy(Map<String, Object> constraint);

    boolean isExist(K id);

    T findById(K id);

    List<T> findAll();

    void createOrUpdate(T entity);

    void createOrUpdate(List<T> entity);

    void delete(T entity);

    void deleteById(K id);

    void deleteAll();

}