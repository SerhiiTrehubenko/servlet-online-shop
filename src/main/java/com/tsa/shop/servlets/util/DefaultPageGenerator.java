package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.servlets.exceptions.WebServerException;
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

    public InputStream getGeneratedPageAsStream(Map<String, Object> parsedRequest, String pageName)
            throws WebServerException {
        if (Objects.isNull(pageName) || pageName.isEmpty()) {
            throw new WebServerException("the Page Name cannot be null or empty", HttpStatus.NOT_FOUND);
        }
        try {
            CONFIG.setOutputFormat(HTMLOutputFormat.INSTANCE);
            CONFIG.setClassForTemplateLoading(DefaultPageGenerator.class, TEMPLATES_DIR);

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
