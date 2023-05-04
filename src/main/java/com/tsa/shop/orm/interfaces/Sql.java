package com.tsa.shop.orm.interfaces;

import com.tsa.shop.orm.impl.SqlInsertion;

import java.util.List;
import java.util.Objects;

public abstract class Sql {

    protected static final String SELECT = "SELECT ";
    protected static final String FROM = " FROM ";
    protected static final String WHERE = " WHERE ";
    protected static final String EQUALS = "=";
    protected static final String SEMICOLON = ";";
    private static final String EMPTY_DELIMITER = "";
    protected static final String COMA_WHITESPACE_DELIMITER = ", ";
    protected final static String PLACE_HOLDER = "?";

    protected final NameResolver resolver;



    protected Sql(NameResolver resolver) {
        this.resolver = resolver;
    }

    public String generate() {
        throw new UnsupportedOperationException("The Class: [%s] does not implement the method: [generate();]".formatted(this.getClass().getName()));
    }

    public String generateById() {
        throw new UnsupportedOperationException("The Class: [%s] does not implement the method: [generate(Serializable id);]".formatted(this.getClass().getName()));
    }

    public String generateByObject(Object objectToInsert) {
        throw new UnsupportedOperationException("The Class: [%s] does not implement the method: [generate(Object objectToInsert);]".formatted(this.getClass().getName()));
    }

    protected String getQuery(String... args) {
        return String.join(EMPTY_DELIMITER, args);
    }

    protected String toFormattedString(List<String> arguments) {
        return String.join(COMA_WHITESPACE_DELIMITER, arguments);
    }

    protected void validateObject(Object entityToInsert) {
        requiredNotNull(entityToInsert);
        assertComplyWithEntity(entityToInsert);
    }

    private void requiredNotNull(Object entityToInsert) {
        Objects.requireNonNull(entityToInsert, "During initializing %s, provided Object for insertion was null".formatted(SqlInsertion.class.getName()));
    }

    private void assertComplyWithEntity(Object entityToInsert) {
        Class<?> parsedClass = resolver.getEntityClass();
        Class<?> entityClass = entityToInsert.getClass();
        if (!parsedClass.isAssignableFrom(entityClass)) {
            throw new RuntimeException(String.format("Provided Instance: [%s] does not comply to parsed class: [%s]",
                    entityToInsert.getClass().getName(), parsedClass.getName()));
        }
    }
}
