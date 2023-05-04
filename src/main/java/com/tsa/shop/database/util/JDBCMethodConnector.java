package com.tsa.shop.database.util;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;

public enum JDBCMethodConnector {
    LONG(long.class, getMethod("setLong", int.class, long.class)),
    LONG_WRAPPER(java.lang.Long.class, getMethod("setLong", int.class, long.class)),
    STRING(java.lang.String.class, getMethod("setString", int.class, String.class)),
    DOUBLE(double.class, getMethod("setDouble", int.class, double.class)),
    DOUBLE_WRAPPER(java.lang.Double.class, getMethod("setDouble", int.class, double.class)),
    DATE(java.sql.Date.class, getMethod("setDate", int.class, java.sql.Date.class));

    private final Class<?> javaType;
    private final Method preparedStatementMethod;

    JDBCMethodConnector(Class<?> javaType, Method preparedStatementMethod) {
        this.javaType = javaType;
        this.preparedStatementMethod = preparedStatementMethod;
    }

    static Method getMethod(String methodName, Class<?>... args) {
        try {
            return PreparedStatement.class.getDeclaredMethod(methodName, args[0], args[1]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getPreparedStatementMethod(Class<?> fieldType) {
        for (JDBCMethodConnector value : JDBCMethodConnector.values()) {
            if (value.javaType.isAssignableFrom(fieldType)) {
                return value.preparedStatementMethod;
            }
        }
        throw new RuntimeException("Provided type: [%s] is wrong".formatted(fieldType.getName()));
    }
}
