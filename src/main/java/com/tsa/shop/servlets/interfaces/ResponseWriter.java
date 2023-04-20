package com.tsa.shop.servlets.interfaces;

import com.tsa.shop.servlets.exceptions.WebServerException;

public interface ResponseWriter {
    void write(Response response) throws WebServerException;
    ResponseWriter createInstance();
}
