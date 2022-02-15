package net.qsef1256.dacobot.database;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface DaoCommon<T, ID extends Serializable> {

    @Deprecated
    Session getCurrentSession();

    long count();

    @Deprecated
    void create(T entity);

    @Deprecated
    void create(List<T> entity);

    @Deprecated
    void update(T entity);

    @Deprecated
    void update(List<T> entity);

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
     * @return T
     * @see #findById(ID)
     */
    List<T> findBy(Map<String, Object> constraint);

    boolean existsById(ID id);

    T findById(ID id);

    List<T> findAll();

    void save(T entity);

    void saveAll(List<T> entity);

    void delete(T entity);

    void deleteById(ID id);

    void deleteAll();

}