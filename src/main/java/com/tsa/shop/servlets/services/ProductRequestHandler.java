package com.tsa.shop.servlets.services;

import com.tsa.shop.ServletStarter;
import com.tsa.shop.domain.dto.ProductDto;
import com.tsa.shop.domain.interfaces.EntityService;
import com.tsa.shop.domain.services.DefaultProductService;
import com.tsa.shop.orm.util.EntityParser;
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
import java.util.Objects;

public class ProductRequestHandler implements RequestHandler {

    private final EntityService<ProductDto> service;

    public ProductRequestHandler() {
        service = new DefaultProductService();
    }

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> parsedRequest = RequestParser.parseRequest(request);

        UriPageConnector pageUri = RequestParser.getUriConnector(parsedRequest, ServletStarter.CASHED_URI);

        try {
            if (Objects.equals(UriPageConnector.PRODUCT_GET_ALL, pageUri)) {

                List<ProductDto> productDtoList = service.findAll();

                parsedRequest.put("products", productDtoList);

                InputStream generatedPage = PageGenerator.getGeneratedPageAsStream(parsedRequest, pageUri);
                ResponseWriter.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), generatedPage);

            } else if (Objects.equals(UriPageConnector.PRODUCT_DELETE, pageUri)) {

                Long id = RequestParser.getId(parsedRequest);
                service.delete(id);

                redirect(response, UriPageConnector.PRODUCT_GET_ALL.getUri());

            } else if (Objects.equals(UriPageConnector.PRODUCTS_EDIT, pageUri)) {

                Long id = RequestParser.getId(parsedRequest);
                ProductDto productDto = service.findById(id);
                parsedRequest.put("productDto", productDto);

                InputStream generatedPage = PageGenerator.getGeneratedPageAsStream(parsedRequest, pageUri);
                ResponseWriter.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), generatedPage);

            } else if (Objects.equals(UriPageConnector.PRODUCT_ADD, pageUri)) {

                InputStream generatedPage = PageGenerator.getGeneratedPageAsStream(parsedRequest, pageUri);
                ResponseWriter.writeSuccessResponse(response, HttpStatus.OK.getStatusCode(), generatedPage);

            } else {
                throw new WebServerException("the Page is not found", HttpStatus.NOT_FOUND);
            }

        } catch (WebServerException e) {
            ResponseWriter.writeErrorResponse(response, parsedRequest, e);
        }
    }

    @Override
    public void handlePost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parsedRequest = RequestParser.parseRequest(request);

        UriPageConnector pageUri = RequestParser.getUriConnector(parsedRequest, ServletStarter.CASHED_URI);
        try {
            if (Objects.equals(UriPageConnector.PRODUCTS_EDIT_SEND, pageUri)) {

                var parameters = RequestParser.getParameters(parsedRequest);
                ProductDto productDto = EntityParser.getDtoInstance(ProductDto.class, parameters);

                service.update(productDto);
                redirect(response, UriPageConnector.PRODUCT_GET_ALL.getUri());

            } else if (Objects.equals(UriPageConnector.PRODUCT_ADD, pageUri)) {

                var parameters = RequestParser.getParameters(parsedRequest);
                ProductDto productDto = EntityParser.getDtoInstance(ProductDto.class, parameters);

                service.add(productDto);
                redirect(response, UriPageConnector.PRODUCT_GET_ALL.getUri());

            } else {
                throw new WebServerException("the Page is not found", HttpStatus.NOT_FOUND);
            }

        } catch (WebServerException e) {
            ResponseWriter.writeErrorResponse(response, parsedRequest, e);
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
