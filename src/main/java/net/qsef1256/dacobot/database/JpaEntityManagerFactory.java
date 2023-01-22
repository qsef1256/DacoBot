package net.qsef1256.dacobot.database;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import lombok.Getter;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.util.ReflectionUtil;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;

import javax.sql.DataSource;
import java.util.*;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * Copyright (c) 2017 Eugen Paraschiv
 * <p>Licensed under MIT
 * <a href="https://github.com/eugenp/tutorials/blob/master/persistence-modules/hibernate-jpa/src/main/java/com/baeldung/hibernate/jpabootstrap/config/JpaEntityManagerFactory.java">Original Source</a>
 */
public class JpaEntityManagerFactory {

    @Getter
    private static Configuration setting;

    private final Class<?>[] entityClasses;

    static {
        try {
            setting = new Configurations().properties(DiaSetting.SETTING_NAME + ".properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public JpaEntityManagerFactory() {
        final Set<Class<?>> annotated =
                ReflectionUtil.getReflections().get(SubTypes.of(TypesAnnotated.with(Entity.class)).asClass());
        this.entityClasses = annotated.toArray(new Class[0]);
    }

    protected EntityManagerFactory getEntityManagerFactory() {
        PersistenceUnitInfo persistenceUnitInfo = getPersistenceUnitInfo(setting.getString("main.package"));
        Map<String, Object> configuration = new HashMap<>();
        return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(persistenceUnitInfo), configuration).build();
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

        config.put("hibernate.dialect", setting.getString("hibernate.dialect"));
        config.put("hibernate.archive.autodetection", setting.getString("hibernate.archive.autodetection"));
        config.put("hibernate.physical_naming_strategy", setting.getString("hibernate.physical_naming_strategy"));
        config.put("hibernate.current_session_context_class", setting.getString("hibernate.current_session_context_class"));

        config.put("hibernate.connection.provider_class", setting.getString("hibernate.connection.provider_class"));
        config.put("hibernate.id.new_generator_mappings", setting.getString("hibernate.id.new_generator_mappings"));

        config.put("hibernate.format_sql", setting.getString("hibernate.format_sql"));
        config.put("hibernate.show_sql", setting.getString("hibernate.show_sql"));
        config.put("hibernate.use_sql_comments", setting.getString("hibernate.use_sql_comments"));
        config.put("hibernate.generate_statistics", setting.getString("hibernate.generate_statistics"));

        config.put("hibernate.hbm2ddl.auto", setting.getString("hibernate.hbm2ddl.auto"));

        config.put("hibernate.hikari.minimumIdle", setting.getString("hibernate.hikari.minimumIdle"));
        config.put("hibernate.hikari.maximumPoolSize", setting.getString("hibernate.hikari.maximumPoolSize"));
        config.put("hibernate.hikari.idleTimeout", setting.getString("hibernate.hikari.idleTimeout"));
        config.put("hibernate.hikari.driverClassName", setting.getString("hibernate.hikari.driverClassName"));
        config.put("hibernate.hikari.jdbcUrl", setting.getString("hibernate.hikari.jdbcUrl"));
        config.put("hibernate.hikari.dataSource.user", setting.getString("hibernate.hikari.dataSource.user"));
        config.put("hibernate.hikari.dataSource.password", setting.getString("hibernate.hikari.dataSource.password"));
        return config;
    }

    protected Class<?>[] getEntities() {
        return entityClasses;
    }

    protected DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(setting.getString("db.driver"));
        dataSource.setUsername(setting.getString("db.user"));
        dataSource.setPassword(setting.getString("db.password"));
        dataSource.setUrl(setting.getString("db.url"));

        return dataSource;
    }

}
