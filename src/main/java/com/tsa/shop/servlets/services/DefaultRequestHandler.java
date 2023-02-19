package com.tsa.shop.servlets.services;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.RequestHandler;
import com.tsa.shop.servlets.util.PageGenerator;
import com.tsa.shop.servlets.util.ResponseWriter;
import com.tsa.shop.servlets.util.SourceFilesProvider;
import com.tsa.shop.servlets.util.RequestParser;
import com.tsa.shop.servlets.enums.UriPageConnector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class DefaultRequestHandler implements RequestHandler {

    private final RequestParser requestParser;
    private final PageGenerator pageGenerator;
    private final ResponseWriter writer;
    private final SourceFilesProvider sourceFilesProvider;

    public DefaultRequestHandler() {
        this(new RequestParser(), new PageGenerator(),
                new ResponseWriter(), new SourceFilesProvider());
    }

    public DefaultRequestHandler(RequestParser requestParser, PageGenerator pageGenerator,
                                 ResponseWriter writer, SourceFilesProvider sourceFilesProvider) {
        this.requestParser = requestParser;
        this.pageGenerator = pageGenerator;
        this.writer = writer;
        this.sourceFilesProvider = sourceFilesProvider;
    }

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parsedRequest = requestParser.parseRequest(request);

        UriPageConnector pageConnector = requestParser.getUriPageConnector(parsedRequest);

        try {
            if (UriPageConnector.HOME == pageConnector || UriPageConnector.SLASH == pageConnector) {
                InputStream generatedPage = pageGenerator.getGeneratedPageAsStream(parsedRequest, pageConnector.getHtmlPage());

                writer.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), generatedPage);

            } else if (Objects.isNull(pageConnector)) {
                String fileSourceUri = requestParser.getUri(parsedRequest);

                InputStream fileSourceContent = sourceFilesProvider.getSourceFileAsStream(fileSourceUri);

                writer.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), fileSourceContent);
            }
        } catch (WebServerException e) {
            writer.writeErrorResponse(response, parsedRequest, e, pageGenerator);
        }
    }
}
