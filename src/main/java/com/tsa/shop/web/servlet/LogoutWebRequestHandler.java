package com.tsa.shop.web.servlet;

import com.tsa.shop.domain.UriPageConnector;
import com.tsa.shop.logging.DomainLogger;
import com.tsa.shop.logmessagegenerator.LogMessageGenerator;
import com.tsa.shop.logout.LogoutService;
import com.tsa.shop.web.WebRequestHandler;
import com.tsa.shop.web.interfaces.PageGenerator;
import com.tsa.shop.web.interfaces.Response;
import com.tsa.shop.web.interfaces.ResponseWriter;
import com.tsa.shop.web.interfaces.ServletRequestParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class LogoutWebRequestHandler extends WebRequestHandler {

    private final LogoutService logoutService;

    public LogoutWebRequestHandler(ServletRequestParser servletRequestParser,
                                   PageGenerator pageGenerator,
                                   ResponseWriter responseWriter,
                                   Response response,
                                   DomainLogger logger,
                                   LogMessageGenerator logMessageGenerator,
                                   LogoutService logoutService) {
        super(servletRequestParser, pageGenerator, responseWriter, response, logger, logMessageGenerator);
        this.logoutService = logoutService;
    }

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super.doGet(servletRequest, servletResponse);
        if (servletResponse.getStatus() == 200) {
            redirect(servletResponse, UriPageConnector.HOME.getUri());
        }
    }

    @Override
    protected InputStream handleGetRequest(Map<String, Object> parsedRequest, UriPageConnector uriPageConnector) {
        Cookie tokenCookie = servletRequestParser.getTokenCookie(parsedRequest);
        logoutService.logout(tokenCookie);
        return InputStream.nullInputStream();
    }
}
