package com.tsa.shop.servlets.enums;

public enum HttpStatus {

    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501);

    private final int status;

    public int getStatusCode() {
        return status;
    }

    HttpStatus(int status) {
        this.status = status;
    }
}
