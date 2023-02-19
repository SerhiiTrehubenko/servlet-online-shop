package com.tsa.shop;

import com.tsa.shop.servlets.init.ServletsSetter;
import com.tsa.shop.servlets.util.PropertyParser;
import com.tsa.shop.servlets.util.UriCache;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class ServletStarter {
    @SuppressWarnings("unused")
    private final static UriCache CASHE = new UriCache().setUp();
    private final static PropertyParser PROPERTY_PARSER = new PropertyParser();
    private final static ServletsSetter SERVLETS_SETTER = new ServletsSetter();

    public static void main(String[] args) throws Exception {
        PROPERTY_PARSER.analyseIncomeArgs(args);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        SERVLETS_SETTER.setAllServletsInApp(servletContextHandler);

        Server server = new Server(PROPERTY_PARSER.getServerPort());
        server.setHandler(servletContextHandler);

        server.start();
    }
}
