package com.tsa.shop.servlets.services;

import com.tsa.shop.domain.dto.ProductDto;
import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.services.DefaultProductService;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;
import com.tsa.shop.servlets.interfaces.RequestHandler;
import com.tsa.shop.servlets.util.PageGenerator;
import com.tsa.shop.servlets.util.RequestParser;
import com.tsa.shop.servlets.util.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ProductRequestHandler implements RequestHandler {

    private final static String PAGE_NOT_FOUND_MESSAGE = "the Page is not found";
    private final EntityService<ProductDto> service;
    private final RequestParser requestParser;
    private final PageGenerator pageGenerator;
    private final ResponseWriter writer;

    public ProductRequestHandler() {
        this(new DefaultProductService(), new RequestParser(),
                new PageGenerator(), new ResponseWriter());
    }

    public ProductRequestHandler(EntityService<ProductDto> service, RequestParser requestParser,
                                 PageGenerator pageGenerator, ResponseWriter writer) {
        this.service = service;
        this.requestParser = requestParser;
        this.pageGenerator = pageGenerator;
        this.writer = writer;
    }

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> parsedRequest = requestParser.parseRequest(request);

        UriPageConnector pageConnector = requestParser.getUriPageConnector(parsedRequest);

        try {
            if (UriPageConnector.PRODUCTS == pageConnector) {

                List<ProductDto> productDtoList = service.findAll();

                parsedRequest.put("products", productDtoList);

                InputStream generatedPage = pageGenerator.getGeneratedPageAsStream(parsedRequest, pageConnector.getHtmlPage());
                writer.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), generatedPage);

            } else if (UriPageConnector.PRODUCTS_DELETE == pageConnector) {

                Long productId = requestParser.getIdFromRequest(parsedRequest);
                service.delete(productId);

                redirect(response, UriPageConnector.PRODUCTS.getUri());

            } else if (UriPageConnector.PRODUCTS_EDIT == pageConnector) {

                Long productId = requestParser.getIdFromRequest(parsedRequest);
                ProductDto productDto = service.findById(productId);

                parsedRequest.put("productDto", productDto);

                InputStream generatedPage = pageGenerator.getGeneratedPageAsStream(parsedRequest, pageConnector.getHtmlPage());
                writer.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), generatedPage);

            } else if (UriPageConnector.PRODUCTS_ADD == pageConnector) {

                InputStream generatedPage = pageGenerator.getGeneratedPageAsStream(parsedRequest, pageConnector.getHtmlPage());
                writer.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), generatedPage);

            } else {
                throw new WebServerException(PAGE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
            }

        } catch (WebServerException e) {
            writer.writeErrorResponse(response, parsedRequest, e, pageGenerator);
        }
    }

    @Override
    public void handlePost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parsedRequest = requestParser.parseRequest(request);

        UriPageConnector pageConnector = requestParser.getUriPageConnector(parsedRequest);

        try {
            if (UriPageConnector.PRODUCTS_EDIT_SEND == pageConnector) {

                var parameters = requestParser.getParameters(parsedRequest);

                service.update(parameters);
                redirect(response, UriPageConnector.PRODUCTS.getUri());

            } else if (UriPageConnector.PRODUCTS_ADD == pageConnector) {

                var parameters = requestParser.getParameters(parsedRequest);

                service.add(parameters);
                redirect(response, UriPageConnector.PRODUCTS.getUri());

            } else {
                throw new WebServerException(PAGE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
            }

        } catch (WebServerException e) {
            writer.writeErrorResponse(response, parsedRequest, e, pageGenerator);
        }
    }

    private void redirect(HttpServletResponse response, String uri) throws WebServerException {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
