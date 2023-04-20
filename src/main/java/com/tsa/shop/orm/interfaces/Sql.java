package com.tsa.shop.orm.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class Sql {

    protected static final String SELECT = "SELECT ";
    protected static final String FROM = " FROM ";
    protected static final String WHERE = " WHERE ";
    protected static final String EQUALS = "=";
    protected static final String SEMICOLON = ";";
    private static final String EMPTY_DELIMITER = "";
    protected static final String COMA_WHITESPACE_DELIMITER = ", ";

    protected final EntityClassMeta meta;

    private final static List<Function<String, Object>> functions = List.of(Integer::parseInt, Double::parseDouble, "'%s'"::formatted);

    protected Sql(EntityClassMeta meta) {
        this.meta = meta;
    }

    public String generate() {
        throw new UnsupportedOperationException("The Class: [%s] does not implement the method: [generate();]".formatted(this.getClass().getName()));
    }

    public String generateById(Serializable id) {
        throw new UnsupportedOperationException("The Class: [%s] does not implement the method: [generate(Serializable id);]".formatted(this.getClass().getName()));
    }

    public String generateByObject(Object objectToInsert) {
        throw new UnsupportedOperationException("The Class: [%s] does not implement the method: [generate(Object objectToInsert);]".formatted(this.getClass().getName()));
    }

    protected String getQuery(String... args) {
        return String.join(EMPTY_DELIMITER, args);
    }

    protected String toFormatString(List<String> arguments) {
        return String.join(COMA_WHITESPACE_DELIMITER, arguments);
    }

    protected String resolveId(Serializable id) {
        String idAsString = Objects.toString(id);
        return
                functions.stream()
                        .filter(function -> isResolvable(function, idAsString))
                        .map(function -> resolve(function, idAsString))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("id: [%s] should be Integer, Double or String".formatted(idAsString)));
    }

    private boolean isResolvable(Function<String, Object> function, String id) {
        try {
            function.apply(id);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private String resolve(Function<String, Object> function, String id) {
        return function.apply(id).toString();
    }
}
