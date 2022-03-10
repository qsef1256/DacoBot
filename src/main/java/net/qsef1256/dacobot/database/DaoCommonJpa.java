package net.qsef1256.dacobot.database;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Query;
import java.io.Serializable;

/**
 * JPA 에 맞춘 Dao Interface 입니다.
 *
 * <p>
 * 사용전 open() 을, 사용후 close() 를 호출해야 합니다.
 * </p>
 *
 * @param <T>  Entity
 * @param <ID> Entity's Key
 */
public interface DaoCommonJpa<T, ID extends Serializable> extends DaoCommon<T, ID>, AutoCloseable {

    void saveAndClose(T entity);

    void saveAllAndClose(@NotNull Iterable<T> entities);

    void open();

    /**
     * 트랜젝션을 유지하기 위해, 저장만 합니다. 추후 close() 를 호출해야 합니다.
     */
    void commit();

    void rollback();

    Query createQuery(String s);

}
