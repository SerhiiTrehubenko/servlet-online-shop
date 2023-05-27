package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.impl.DefaultServletRequestParser;
import com.tsa.shop.servlets.impl.UriCache;
import com.tsa.shop.servlets.interfaces.ServletRequestParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultServletRequestParserTest {

    @Mock
    private HttpServletRequest request;
    private final static UriCache CASHE = new UriCache().setUp();
    private final ServletRequestParser parser = new DefaultServletRequestParser(CASHE);
    @Test
    void shouldReturnValidParsedHttpRequestAsMap() {
        String expectedMethod = "GET";
        String expectedUri = "/home";
        String expectedUrl = "http://localhost:3001/home";
        String expectedPathInfo = "http://localhost:3001";
        String expectedParametersId = "15";

        mockHttpRequest();
        Map<String, Object> parsedRequest = parser.parseRequest(request);

        assertEquals(expectedMethod, parsedRequest.get("method"));
        assertEquals(expectedUri, parsedRequest.get("URI"));
        assertEquals(expectedUrl, parsedRequest.get("URL").toString());
        assertEquals(expectedPathInfo, parsedRequest.get("pathInfo"));

        @SuppressWarnings("unchecked")
        Map<String, String[]> parameters = (Map<String, String[]>) parsedRequest.get("parameters");
        assertEquals(expectedParametersId, parameters.get("id")[0]);
    }

    @Test
    void shouldReturnIdOfProductFromMapOfParsedHttpRequest() {
        mockHttpRequest();
        Long expectedId = 15L;

        Map<String, Object> parsedRequest = parser.parseRequest(request);
        Long id = parser.getIdFromRequest(parsedRequest);

        assertEquals(expectedId, id);
    }

    @Test
    void shouldReturnMapOfParametersFromMapOfParsedHttpRequest() {
        mockHttpRequest();
        String expectedId = "15";
        String expectedName = "comp";
        String expectedPrice = "65.55";
        String expectedDate = "2023-01-10";

        Map<String, Object> parsedRequest = parser.parseRequest(request);

        Map<String, String[]> expectedParameters = parser.getParameters(parsedRequest);

        assertEquals(expectedId, expectedParameters.get("id")[0]);
        assertEquals(expectedName, expectedParameters.get("name")[0]);
        assertEquals(expectedPrice, expectedParameters.get("price")[0]);
        assertEquals(expectedDate, expectedParameters.get("date")[0]);
    }

    @Test
    void getUriConnector() {
        mockHttpRequest();
        String expectedPageName = "home.html";

        Map<String, Object> parsedRequest = parser.parseRequest(request);
        UriPageConnector uriPageConnector = parser.getUriPageConnector(parsedRequest);

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
        Cookie cookie = new Cookie("user-token", "token");


        when(request.getMethod()).thenReturn(expectedMethod);
        when(request.getRequestURI()).thenReturn(expectedUri);
        when(request.getRequestURL()).thenReturn(expectedUrl);
        when(request.getPathInfo()).thenReturn(expectedPathInfo);
        when(request.getParameterMap()).thenReturn(expectedParameters);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    }
}