package net.qsef1256.dacobot.database.inject;

import com.google.inject.assistedinject.Assisted;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;

import java.io.Serializable;

public interface DaoCommonJpaFactory<T, K extends Serializable> {

    DaoCommonJpaImpl<T, K> create(@Assisted Class<T> clazz);

}