package net.qsef1256.dacobot.database;

import java.io.Serializable;
import java.util.List;

public interface DaoCommonOld<T, ID extends Serializable> extends DaoCommon<T, ID> {

    @Deprecated
    void create(T entity);

    @Deprecated
    void create(List<T> entity);

    @Deprecated
    void update(T entity);

    @Deprecated
    void update(List<T> entity);

}
