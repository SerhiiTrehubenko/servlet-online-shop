package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UriCache {
    private final static Map<String, UriPageConnector> CACHE = new ConcurrentHashMap<>();
    public UriCache setUp(){
        for (UriPageConnector value : UriPageConnector.values()) {
            CACHE.put(value.getUri(), value);
        }
        return this;
    }

    public static UriPageConnector getUriPageConnector(String keyUri) {
        return CACHE.get(keyUri);
    }
}
