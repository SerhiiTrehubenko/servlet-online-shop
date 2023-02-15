package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public class ResponseWriter {
    private final static int DEFAULT_BUFF_SIZE = 8 * 1024;

    public static void writeSuccessResponse(HttpServletResponse response,
                                            int httpStatus,
                                            InputStream bodyOfResponse) throws WebServerException {

        byte[] buff = new byte[DEFAULT_BUFF_SIZE];
        int count;
        response.setStatus(httpStatus);

        try {
            while ((count = bodyOfResponse.read(buff)) != -1) {
                response.getOutputStream().write(buff, 0, count);
            }
        } catch (IOException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void writeErrorResponse(HttpServletResponse response,
                                          Map<String, Object> parsedRequest,
                                          WebServerException e) {
        parsedRequest.put("errorMessage", e.getMessage());
        InputStream generatedPage = PageGenerator.getGeneratedPageAsStream(parsedRequest);

        writeSuccessResponse(response, e.getStatus().getStatusCode(), generatedPage);

        System.out.println("HttpRequest URL: " + parsedRequest.get("URL") + " Time of request: " + new Date());
        e.printStackTrace();
    }
}
