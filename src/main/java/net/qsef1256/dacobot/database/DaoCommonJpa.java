package net.qsef1256.dacobot.database;

import jakarta.persistence.Query;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * JPA 에 맞춘 Dao Interface 입니다.
 *
 * <p>
 * 사용전 open() 을, 사용후 close() 를 호출해야 합니다.
 * </p>
 *
 * @param <T> Entity
 * @param <K> Entity's Key
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @deprecated use Spring Data JPA method.
 */
@Deprecated(since = "use Spring Data JPA method")
public interface DaoCommonJpa<T, K extends Serializable> extends DaoCommon<T, K>, AutoCloseable {

    /**
     * 트랜젝션을 엽니다.
     */
    void open();

    /**
     * 트랜젝션을 유지하기 위해, 저장만 합니다. 추후 close() 를 호출해야 합니다.
     */
    void commit();

    void rollback();

    /**
     * 트랜젝션을 닫습니다.
     */
    @Override
    void close();

    void saveAndClose(T entity);

    void saveAllAndClose(@NotNull Iterable<T> entities);

    Query createQuery(String s);

}
