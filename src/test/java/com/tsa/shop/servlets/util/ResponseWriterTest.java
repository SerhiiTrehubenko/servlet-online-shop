package com.tsa.shop.servlets.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseWriterTest {

    @Mock
    HttpServletResponse response;

//    @Test
//    void name() throws IOException {
//        var out = new ByteArrayOutputStream();
//        when(response.getOutputStream()).thenReturn(ServletOutputStream);
//    }
}