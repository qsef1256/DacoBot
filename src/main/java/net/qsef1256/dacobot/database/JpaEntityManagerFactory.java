package net.qsef1256.dacobot.database;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import net.qsef1256.dacobot.setting.DiaSetting;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * Copyright (c) 2017 Eugen Paraschiv
 * <p>Licensed under MIT
 * <a href="https://github.com/eugenp/tutorials/blob/master/persistence-modules/hibernate-jpa/src/main/java/com/baeldung/hibernate/jpabootstrap/config/JpaEntityManagerFactory.java">Original Source</a>
 */
@Component
public class JpaEntityManagerFactory {

    private final DiaSetting setting;
    private final Class<?>[] entityClasses;

    @Autowired
    public JpaEntityManagerFactory(@NotNull Reflections reflections,
                                   @NotNull DiaSetting setting) {
        final Set<Class<?>> annotated = reflections.get(SubTypes
                .of(TypesAnnotated.with(Entity.class))
                .asClass());
        this.setting = setting;
        entityClasses = annotated.toArray(new Class[0]);
    }

    protected EntityManagerFactory getEntityManagerFactory() {
        PersistenceUnitInfo persistenceUnitInfo = getPersistenceUnitInfo(setting.getMainPackage());
        Map<String, Object> configuration = new HashMap<>();

        return new EntityManagerFactoryBuilderImpl(
                new PersistenceUnitInfoDescriptor(persistenceUnitInfo),
                configuration).build();
    }

    protected PersistenceUnitInfo getPersistenceUnitInfo(String name) {
        return new PersistenceUnitInfoImpl(name, getEntityClassNames(), getProperties())
                .setJtaDataSource(getDataSource())
                .setNonJtaDataSource(getDataSource());
    }

    protected List<String> getEntityClassNames() {
        return Arrays.stream(getEntities())
                .map(Class::getName)
                .toList();
    }

    protected Properties getProperties() {
        Properties config = new Properties();

        config.put("hibernate.archive.autodetection", setting.getSetting().getString("hibernate.archive.autodetection"));
        config.put("hibernate.physical_naming_strategy", setting.getSetting().getString("hibernate.physical_naming_strategy"));
        config.put("hibernate.current_session_context_class", setting.getSetting().getString("hibernate.current_session_context_class"));

        config.put("hibernate.connection.provider_class", setting.getSetting().getString("hibernate.connection.provider_class"));
        config.put("hibernate.id.new_generator_mappings", setting.getSetting().getString("hibernate.id.new_generator_mappings"));

        config.put("hibernate.format_sql", setting.getSetting().getString("hibernate.format_sql"));
        config.put("hibernate.show_sql", setting.getSetting().getString("hibernate.show_sql"));
        config.put("hibernate.use_sql_comments", setting.getSetting().getString("hibernate.use_sql_comments"));
        config.put("hibernate.generate_statistics", setting.getSetting().getString("hibernate.generate_statistics"));

        config.put("hibernate.hbm2ddl.auto", setting.getSetting().getString("hibernate.hbm2ddl.auto"));

        config.put("hibernate.hikari.minimumIdle", setting.getSetting().getString("hibernate.hikari.minimumIdle"));
        config.put("hibernate.hikari.maximumPoolSize", setting.getSetting().getString("hibernate.hikari.maximumPoolSize"));
        config.put("hibernate.hikari.idleTimeout", setting.getSetting().getString("hibernate.hikari.idleTimeout"));
        config.put("hibernate.hikari.driverClassName", setting.getSetting().getString("hibernate.hikari.driverClassName"));
        config.put("hibernate.hikari.jdbcUrl", setting.getSetting().getString("hibernate.hikari.jdbcUrl"));
        config.put("hibernate.hikari.dataSource.user", setting.getSetting().getString("hibernate.hikari.dataSource.user"));
        config.put("hibernate.hikari.dataSource.password", setting.getSetting().getString("hibernate.hikari.dataSource.password"));

        return config;
    }

    protected Class<?>[] getEntities() {
        return entityClasses;
    }

    protected DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(setting.getSetting().getString("db.driver"));
        dataSource.setUsername(setting.getSetting().getString("db.user"));
        dataSource.setPassword(setting.getSetting().getString("db.password"));
        dataSource.setUrl(setting.getSetting().getString("db.url"));

        return dataSource;
    }

}
