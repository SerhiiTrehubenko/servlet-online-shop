package com.tsa.shop.web.interfaces;

public interface ResponseWriter {
    void write(Response response);

    ResponseWriter createInstance();
}
