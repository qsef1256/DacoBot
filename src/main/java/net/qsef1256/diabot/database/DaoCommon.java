package net.qsef1256.diabot.database;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface DaoCommon<K extends Serializable, T> {

    Session getCurrentSession();

    void create(T entity);

    void create(List<T> entity);

    void update(T entity);

    void update(List<T> entity);

    /**
     * 제약 조건들로 아이템을 찾습니다. 결과는 여러개 일 수 있습니다.
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
     * @see #findById(K)
     */
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