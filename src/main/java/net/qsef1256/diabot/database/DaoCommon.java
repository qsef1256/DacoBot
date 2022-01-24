package net.qsef1256.diabot.database;

import java.io.Serializable;
import java.util.List;

public interface DaoCommon<K extends Serializable, T> {

    void create(T entity);

    void update(T entity);

    boolean isExist(K id);

    List<T> findAll();

    T findById(K id);

    void delete(T entity);

    void deleteById(K id);

    void deleteAll();

}