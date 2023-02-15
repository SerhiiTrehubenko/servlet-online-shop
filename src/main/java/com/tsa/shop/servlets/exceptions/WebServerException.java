package com.tsa.shop.servlets.exceptions;

import com.tsa.shop.servlets.enums.HttpStatus;

public class WebServerException extends RuntimeException {

    private final HttpStatus status;

    public WebServerException(HttpStatus status) {
        this.status = status;
    }

    public WebServerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public WebServerException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
