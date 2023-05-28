package com.tsa.shop.web.interfaces;

import java.io.InputStream;

public interface ContentFileProvider {
    InputStream getSourceFileAsStream(String uriFromRequest);
}
