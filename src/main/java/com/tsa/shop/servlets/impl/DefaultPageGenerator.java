package com.tsa.shop.servlets.impl;

import com.tsa.shop.domain.HttpStatus;
import com.tsa.shop.domain.WebServerException;
import com.tsa.shop.servlets.interfaces.PageGenerator;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class DefaultPageGenerator implements PageGenerator {

    private static final Configuration CONFIG = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    private static final String TEMPLATES_DIR = "/templates";

    @Override
    public InputStream getGeneratedPageAsStream(String pageName) {
        Map<String, Object> emptyContent = Map.of();
        return getGeneratedPageAsStream(emptyContent, pageName);
    }

    public InputStream getGeneratedPageAsStream(Map<String, Object> parsedRequest, String pageName) {
        if (Objects.isNull(pageName) || pageName.isEmpty()) {
            throw new IllegalArgumentException("the Page Name cannot be null or empty");
        }
        try {
            CONFIG.setOutputFormat(HTMLOutputFormat.INSTANCE);
            CONFIG.setClassForTemplateLoading(DefaultPageGenerator.class, TEMPLATES_DIR);

            StringWriter writer = new StringWriter();

            Template template = CONFIG.getTemplate(pageName);
            template.process(parsedRequest, writer);

            return new ByteArrayInputStream(writer.toString().getBytes());
        } catch (TemplateNotFoundException e) {
            throw new WebServerException("The Http page is not found", e, HttpStatus.NOT_FOUND, this);
        } catch (IOException | TemplateException e) {
            throw new WebServerException("There was a problem during the http page generation", e, HttpStatus.INTERNAL_SERVER_ERROR, this);
        }
    }
}
