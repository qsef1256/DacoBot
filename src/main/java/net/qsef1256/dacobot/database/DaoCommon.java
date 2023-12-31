package net.qsef1256.dacobot.database;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

// TODO: replace to CrudRepository

/**
 * CRUD 를 포함하는 일반 Dao Interface 입니다.
 *
 * @param <T> Entity
 * @param <K> Entity's Key
 * @see org.springframework.data.repository.CrudRepository
 * @deprecated use Spring Data JPA method.
 */
@Deprecated(since = "use Spring Data JPA method")
public interface DaoCommon<T, K extends Serializable> {

    void save(T entity);

    void saveAll(Iterable<T> entity);

    boolean existsById(K id);

    /**
     * 제약 조건들로 엔티티 들을 찾습니다. 결과는 여러개 일 수 있습니다.
     *
     * <p>예시:
     * <pre>{@code
     * Map<String, Object> constraint = new HashMap<>();
     * constraint.put("ownerId", 419761037861060619L);
     *
     * List<PaintEntity> paints = dao.findBy(constraint);
     * }</pre>
     *
     * @param constraint constraint Map
     * @return List of T
     * @see #findById(K)
     */
    List<T> findBy(Map<String, Object> constraint);

    T findById(K id);

    List<T> findAll();

    long count();

    void delete(T entity);

    void deleteById(K id);

    void deleteAll();

}
