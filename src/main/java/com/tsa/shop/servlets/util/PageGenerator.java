package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.enums.UriPageConnector;
import com.tsa.shop.servlets.exceptions.WebServerException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

import java.io.*;
import java.util.Map;
import java.util.Objects;

public class PageGenerator {

    private static final Configuration CONFIG = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    private static final String TEMPLATES_DIR = "/templates";

    private static final String ERROR_PAGE = "error-page.html";

    public static InputStream getGeneratedPageAsStream(Map<String, Object> parsedRequest, UriPageConnector pageUri)
            throws WebServerException {

        String pageName = pageUri.getHtmlPage();
        return getGeneratedPageAsStream(parsedRequest, pageName);
    }

    public static InputStream getGeneratedPageAsStream(Map<String, Object> parsedRequest)
            throws WebServerException {

        return getGeneratedPageAsStream(parsedRequest, ERROR_PAGE);
    }

    public static InputStream getGeneratedPageAsStream(Map<String, Object> parsedRequest, String pageName)
            throws WebServerException {
        if (Objects.isNull(pageName) || pageName.isEmpty()) {
            throw new WebServerException("the Page Name cannot be null or empty", HttpStatus.NOT_FOUND);
        }
        try {
            CONFIG.setClassForTemplateLoading(PageGenerator.class, TEMPLATES_DIR);

            StringWriter writer = new StringWriter();

            Template template = CONFIG.getTemplate(pageName);
            template.process(parsedRequest, writer);

            return new ByteArrayInputStream(writer.toString().getBytes());
        } catch (TemplateNotFoundException e) {
            throw new WebServerException(e, HttpStatus.NOT_FOUND);
        } catch (IOException | TemplateException e) {
            throw new WebServerException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
