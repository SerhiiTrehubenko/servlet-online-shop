package com.tsa.shop.servlets.interfaces;

import com.tsa.shop.servlets.exceptions.WebServerException;

import java.io.InputStream;

public interface ContentFileProvider {
    InputStream getSourceFileAsStream(String uriFromRequest) throws WebServerException;
}
