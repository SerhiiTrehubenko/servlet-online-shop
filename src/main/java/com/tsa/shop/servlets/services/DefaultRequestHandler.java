package com.tsa.shop.servlets.services;

import com.tsa.shop.ServletStarter;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.RequestHandler;
import com.tsa.shop.servlets.util.PageGenerator;
import com.tsa.shop.servlets.util.ResponseWriter;
import com.tsa.shop.servlets.util.SourceProvider;
import com.tsa.shop.servlets.util.RequestParser;
import com.tsa.shop.servlets.enums.UriPageConnector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Map;
import java.util.Objects;

public class DefaultRequestHandler implements RequestHandler {

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parsedRequest = RequestParser.parseRequest(request);

        String uriFromParsedRequest = String.valueOf(parsedRequest.get("URI"));
        UriPageConnector pageUri = RequestParser.getUriConnector(parsedRequest, ServletStarter.CASHED_URI);

        try {
            if (Objects.equals(UriPageConnector.HOME, pageUri) || Objects.equals(UriPageConnector.HOME_SLASH, pageUri)) {

                InputStream generatedPage = PageGenerator.getGeneratedPageAsStream(parsedRequest, pageUri);
                ResponseWriter.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), generatedPage);
            } else if (Objects.isNull(pageUri)) {

                InputStream inputPageContent = SourceProvider.getSourceFileContent(uriFromParsedRequest);
                ResponseWriter.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), inputPageContent);
            }
        } catch (WebServerException e) {
            ResponseWriter.writeErrorResponse(response, parsedRequest, e);
        }
    }
}
