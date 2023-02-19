package com.tsa.shop.servlets.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RequestHandler {
    void handleGet(HttpServletRequest request, HttpServletResponse response);

    default void handlePost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("NOT IMPLEMENTED");
    }
}
