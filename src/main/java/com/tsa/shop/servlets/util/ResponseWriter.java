package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public class ResponseWriter {
    private final static String ERROR_PAGE = "error-page.html";
    private final static int DEFAULT_BUFF_SIZE = 8 * 1024;

    public void writeSuccessResponse(HttpServletResponse response,
                                     int httpStatus,
                                     InputStream bodyOfResponse) throws WebServerException {
        byte[] buffer = new byte[DEFAULT_BUFF_SIZE];
        int count;
        response.setStatus(httpStatus);

        try {
            while ((count = bodyOfResponse.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void writeErrorResponse(HttpServletResponse response,
                                   Map<String, Object> parsedRequest,
                                   WebServerException e,
                                   PageGenerator pageGenerator) {
        parsedRequest.put("errorMessage", e.getMessage());
        InputStream errorPage = pageGenerator.getGeneratedPageAsStream(parsedRequest, ERROR_PAGE);

        writeSuccessResponse(response, e.getStatus().getStatusCode(), errorPage);

        printErrorReport(parsedRequest.get("URL"), e);
    }
    private void printErrorReport(Object requestUri, WebServerException e) {
        System.out.println("HttpRequest URL: " + requestUri + " Time of request: " + new Date());
        e.printStackTrace();
    }
}
