package net.qsef1256.dacobot.database;

import java.io.Serializable;
import java.util.List;

@Deprecated
public interface DaoCommonOld<T, ID extends Serializable> extends DaoCommon<T, ID> {

    void create(T entity);

    void create(List<T> entity);

    void update(T entity);

    void update(List<T> entity);

}
