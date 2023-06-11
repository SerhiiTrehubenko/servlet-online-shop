package com.tsa.shop.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainLoggerImpl implements DomainLogger {

    private Logger logger;

    @Override
    public DomainLogger getLogger(Class<?> classToBeLogged) {
        DomainLoggerImpl domainLogger = new DomainLoggerImpl();
        domainLogger.setLogger(LoggerFactory.getLogger(classToBeLogged));
        return  domainLogger;
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
