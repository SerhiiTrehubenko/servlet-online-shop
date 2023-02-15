package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;

import java.util.HashMap;
import java.util.Map;

public class UriCacher {
    public static Map<String, UriPageConnector> setCache(){
        Map<String, UriPageConnector> mapCache = new HashMap<>();

        for (UriPageConnector value : UriPageConnector.values()) {
            mapCache.put(value.getUri(), value);
        }

        return mapCache;
    }
}
