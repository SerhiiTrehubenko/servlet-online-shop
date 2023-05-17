package com.tsa.shop.servlets.interfaces;

import java.io.InputStream;

public interface ContentFileProvider {
    InputStream getSourceFileAsStream(String uriFromRequest);
}
