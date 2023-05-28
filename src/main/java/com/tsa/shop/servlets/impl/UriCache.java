package com.tsa.shop.servlets.impl;

import com.tsa.shop.domain.UriPageConnector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UriCache {
    private final Map<String, UriPageConnector> cache = new ConcurrentHashMap<>();
    public UriCache setUp(){
        for (UriPageConnector value : UriPageConnector.values()) {
            cache.put(value.getUri(), value);
        }
        return this;
    }

    public UriPageConnector getUriPageConnector(String keyUri) {
        return cache.get(keyUri);
    }
}
