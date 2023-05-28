package com.tsa.shop.domain;

public enum HttpStatus {

    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    UNAVALIABLE(503);

    private final int status;

    public int getCode() {
        return status;
    }

    HttpStatus(int status) {
        this.status = status;
    }
}
