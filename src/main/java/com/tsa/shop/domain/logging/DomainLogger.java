package com.tsa.shop.domain.logging;

public interface DomainLogger {
    void setClass(Class<?> classToBeLogged);
    void debug(String message);
    void info(String message);
    void error(String message);
    void warn(String message);
}
