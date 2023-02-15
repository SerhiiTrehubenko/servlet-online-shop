package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestParserTest {

    @Mock
    HttpServletRequest request;


    @Test
    void parseRequest() {
        String expectedMethod = "GET";
        String expectedUri = "/home";
        String expectedUrl = "http://localhost:3001/home";
        String expectedPathInfo = "http://localhost:3001";
        String expectedParametersId = "15";

        mockHttpRequest();
        Map<String, Object> parsedRequest = RequestParser.parseRequest(request);

        assertEquals(expectedMethod, parsedRequest.get("method"));
        assertEquals(expectedUri, parsedRequest.get("URI"));
        assertEquals(expectedUrl, parsedRequest.get("URL").toString());
        assertEquals(expectedPathInfo, parsedRequest.get("pathInfo"));
        assertEquals(expectedParametersId, ((Map<String, String[]>) parsedRequest.get("parameters")).get("id")[0]);
    }

    @Test
    void getId() {
        mockHttpRequest();
        Long expectedId = 15L;

        Map<String, Object> parsedRequest = RequestParser.parseRequest(request);
        Long id = RequestParser.getId(parsedRequest);

        assertEquals(expectedId, id);
    }

    @Test
    void getParameters() {
        mockHttpRequest();
        String expectedId = "15";

        Map<String, Object> parsedRequest = RequestParser.parseRequest(request);

        Map<String, String[]> expectedParameters = RequestParser.getParameters(parsedRequest);

        assertEquals(expectedId, expectedParameters.get("id")[0]);
    }

    @Test
    void getUriConnector() {
        mockHttpRequest();
        String expectedPageName = "home.html";

        Map<String, Object> parsedRequest = RequestParser.parseRequest(request);
        UriPageConnector uriPageConnector = RequestParser.getUriConnector(parsedRequest, UriCacher.setCache());

        assertEquals(expectedPageName, uriPageConnector.getHtmlPage());
    }

    private void mockHttpRequest() {
        String expectedMethod = "GET";
        String expectedUri = "/home";
        StringBuffer expectedUrl = new StringBuffer("http://localhost:3001/home");
        String expectedPathInfo = "http://localhost:3001";
        Map<String, String[]> expectedParameters = Map.of("id", new String[]{"15"},
                "name", new String[]{"comp"},
                "price", new String[]{"65.55"},
                "date", new String[]{"2023-01-10"});


        when(request.getMethod()).thenReturn(expectedMethod);
        when(request.getRequestURI()).thenReturn(expectedUri);
        when(request.getRequestURL()).thenReturn(expectedUrl);
        when(request.getPathInfo()).thenReturn(expectedPathInfo);
        when(request.getParameterMap()).thenReturn(expectedParameters);
    }
}