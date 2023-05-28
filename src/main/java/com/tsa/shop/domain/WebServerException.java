package com.tsa.shop.domain;

public class WebServerException extends RuntimeException {

    private HttpStatus status;
    private String classExceptionThrown = "A class is not provided";
    private static final String DEFAULT_MESSAGE = "Empty message";

    public WebServerException(String message) {
        super(message);
    }

    public WebServerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public WebServerException(String message, HttpStatus status, Object currentClass) {
        super(message);
        this.status = status;
        classExceptionThrown = className(currentClass);
    }

    public WebServerException(Throwable cause, HttpStatus status) {
        super(DEFAULT_MESSAGE, cause);
        this.status = status;
    }

    public WebServerException(String message, Throwable cause, HttpStatus status, Object currentClass) {
        super(message, cause);
        this.status = status;
        classExceptionThrown = className(currentClass);
    }

    public WebServerException(Throwable cause, HttpStatus status, Object currentClass) {
        super(cause);
        this.status = status;
        classExceptionThrown = className(currentClass);
    }

    String className(Object currentClass) {
        return currentClass.getClass().getSimpleName();
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

    public String getOccurrenceClass() {
        return classExceptionThrown;
    }
}
