package net.qsef1256.dacobot.database.hibernate;

import net.qsef1256.dacobot.util.NamingUtil;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.jetbrains.annotations.NotNull;

public class SnakeCasePhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        if (identifier == null) {
            return null;
        }
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        if (identifier == null) {
            return null;
        }
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        if (identifier == null) {
            return null;
        }
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSequenceName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        if (identifier == null) {
            return null;
        }
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        if (identifier == null) {
            return null;
        }
        return convertToSnakeCase(identifier);
    }

    private Identifier convertToSnakeCase(final @NotNull Identifier identifier) {
        return Identifier.toIdentifier(NamingUtil.toSnakeCase(identifier.getText()));
    }

}
