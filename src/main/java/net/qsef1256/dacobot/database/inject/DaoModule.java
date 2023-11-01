package net.qsef1256.dacobot.database.inject;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;

public class DaoModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(DaoCommonJpa.class, DaoCommonJpaImpl.class)
                .build(DaoCommonJpaFactory.class));
    }

}
