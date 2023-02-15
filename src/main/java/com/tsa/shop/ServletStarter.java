package com.tsa.shop;

import com.tsa.shop.servlets.init.ServletsSetter;
import com.tsa.shop.servlets.util.PropertyReader;
import com.tsa.shop.servlets.util.UriCacher;
import com.tsa.shop.servlets.enums.UriPageConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.Map;

public class ServletStarter {
    public final static Map<String, UriPageConnector> CASHED_URI = UriCacher.setCache();
    public final static PropertyReader TUNER = new PropertyReader();

    public static void main(String[] args) throws Exception {
        TUNER.analyseIncomeArgs(args);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        ServletsSetter.setAllServletsInApp(servletContextHandler);

        Server server = new Server(TUNER.getPort());
        server.setHandler(servletContextHandler);

        server.start();

    }
}
