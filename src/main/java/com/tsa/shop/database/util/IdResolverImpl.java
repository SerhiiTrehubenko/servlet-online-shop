package com.tsa.shop.database.util;

import com.tsa.shop.database.interfaces.IdResolver;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class IdResolverImpl implements IdResolver {
    private final static List<Function<String, Object>> FUNCTIONS = List.of(Long::parseLong, Double::parseDouble, "'%s'"::formatted);

    public Serializable resolveId(Serializable id) {
        String idAsString = Objects.toString(id);
        return
                FUNCTIONS.stream()
                        .filter(function -> isResolvable(function, idAsString))
                        .map(function -> resolve(function, idAsString))
                        .findFirst()
                        .orElseThrow(() -> new WebServerException("id: [%s] should be Integer, Double or String".formatted(idAsString), HttpStatus.BAD_REQUEST, this));
    }

    private boolean isResolvable(Function<String, Object> function, String id) {
        try {
            function.apply(id);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private Serializable resolve(Function<String, Object> function, String id) {
        return (Serializable) function.apply(id);
    }
}
