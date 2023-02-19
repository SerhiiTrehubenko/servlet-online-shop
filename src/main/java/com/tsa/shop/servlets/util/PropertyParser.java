package com.tsa.shop.servlets.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;
import java.util.Properties;

public class PropertyParser {
    private static final String SERVER_PORT = "serverPort";
    private static final String DB_URL = "dbUrl";
    private static final String DB_NAME = "dbName";
    private static final String USER_NAME = "userName";
    private static final String PASSWORD = "password";
    private final static int DEFAULT_PORT = 3001;
    private final static int PORT_INCREMENT = 1;
    private final static String FORWARD_SLASH = "/";
    private static Properties properties;
    private final PropertyReader reader;

    public PropertyParser() {
        this(new PropertyReader());
    }

    public PropertyParser(PropertyReader propertyReader) {
        reader = propertyReader;
    }

    public void analyseIncomeArgs(String... args) {
        checkStartUpFirstTime();
        checkArgsNumber(args);
        properties = resolveProperties(args);
    }

    public int getServerPort() {
        String portFromProperty = properties.getProperty(SERVER_PORT);
        int availablePort = getAvailablePort(Objects.isNull(portFromProperty) ? DEFAULT_PORT : Integer.parseInt(portFromProperty));
        System.out.println("Server is running on: " + availablePort + " port");
        return availablePort;
    }

    public String getDbUrl() {
        String dbUrl = properties.getProperty(DB_URL);
        Objects.requireNonNull(dbUrl, "A Data Base URL is absent");
        String dbName = properties.getProperty(DB_NAME);

        return Objects.isNull(dbName) ? dbUrl + FORWARD_SLASH : dbUrl + FORWARD_SLASH + dbName;
    }

    public String getDbUserName() {
        String dbUserName = properties.getProperty(USER_NAME);
        Objects.requireNonNull(dbUserName, "A Data Base USER NAME is absent");
        return dbUserName;
    }

    public String getDbPassword() {
        String dbPassword = properties.getProperty(PASSWORD);
        Objects.requireNonNull(dbPassword, "A Data Base PASSWORD is absent");
        return dbPassword;
    }

    void checkStartUpFirstTime() {
        if (Objects.nonNull(properties)) {
            throw new RuntimeException("properties cannot be changed after Server was started");
        }
    }

    void checkArgsNumber(String... args) {
        if (args.length > 1) {
            throw new RuntimeException("Incorrect arguments number, max number of arguments is 1:" +
                    " arg_1: a path to a \"application.property\" file.");
        }
    }

    Properties resolveProperties(String... args) {
        for (String arg : args) {
            try {
                return reader.getCustomProperties(arg);
            } catch (IOException ignored) {
                // custom properties can be absent
            }
        }
        return reader.getProperties();
    }

    int getAvailablePort(int port) {
        int newPort = port;
        try (var socket = new ServerSocket(newPort)) {
            // try to start socket with provided port
        } catch (IllegalArgumentException e) {
            newPort = DEFAULT_PORT;
            newPort = getAvailablePort(newPort);
        } catch (IOException e) {
            newPort = getAvailablePort(newPort + PORT_INCREMENT);
        }
        return newPort;
    }
}
