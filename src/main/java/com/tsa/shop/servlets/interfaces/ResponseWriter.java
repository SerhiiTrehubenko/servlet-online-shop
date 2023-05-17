package com.tsa.shop.servlets.interfaces;

public interface ResponseWriter {
    void write(Response response);

    ResponseWriter createInstance();
}
