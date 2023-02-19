package com.tsa.shop.servlets.init;

import com.tsa.shop.servlets.annot.Servlet;
import jakarta.servlet.http.HttpServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class ServletsSetter {

    private final static String DEFAULT_SERVLETS_PACKAGE = "com.tsa.shop";

    public void setAllServletsInApp(ServletContextHandler servletContextHandler) {
        List<Class<HttpServlet>> servletClasses = getHttpServletClasses();
        List<HttpServlet> servletInstances = getHttpServletInstances(servletClasses);

        servletInstances.forEach(instance -> {
            try {
                Method methodGetUri = instance.getClass().getDeclaredMethod("getUri");
                Object uri = methodGetUri.invoke(instance);
                servletContextHandler.addServlet(new ServletHolder(instance), String.valueOf(uri));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
    @SuppressWarnings("unchecked")
    List<Class<HttpServlet>> getHttpServletClasses() {
        Reflections reflections = new Reflections(DEFAULT_SERVLETS_PACKAGE, Scanners.TypesAnnotated);
        return reflections.getTypesAnnotatedWith(Servlet.class).stream()
                .map(x -> (Class<HttpServlet>) x)
                .collect(Collectors.toList());
    }

    List<HttpServlet> getHttpServletInstances(List<Class<HttpServlet>> servletClasses) {
        return servletClasses.stream()
                .map(clazz -> {
                    try {
                        return clazz.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
