package com.tsa.shop.servlets.util;

import com.tsa.shop.database.util.PropertyParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;
import java.util.Properties;

public class PropertyReader {

    private final static String DEFAULT_PORT = "3001";

    private Integer port;
    private Properties properties;

    public void analyseIncomeArgs(String... args) {
        stateDefender();
        isEligibleArgsNumber(args);
        port = resolvePort(args);
        properties = resolveProperties(args);
    }

    void stateDefender() {
        if (Objects.nonNull(port) || Objects.nonNull(properties)) {
            throw new RuntimeException("properties cannot be changed after Server was started");
        }
    }
    Properties resolveProperties(String... args) {
        Properties property = null;
        for (String arg : args) {
            try {
                property = PropertyParser.getCustomProperties(arg);
            } catch (IOException ignored) {
                //TODO: ignored
            }
        }
        if (Objects.isNull(property)) {
            property = PropertyParser.getProperties();
        }
        return property;
    }


    int resolvePort(String... args) {
        Integer port = null;
        for (String arg : args) {
            try {
                port = Integer.parseInt(arg);
            } catch (NumberFormatException ignored) {
                //TODO ignored
            }
        }
        if (Objects.isNull(port)) {
            port = Integer.parseInt(DEFAULT_PORT);
        }

        port = getAvailablePort(port);
        System.out.println("Server is running on: " + port + " port");
        return port;
    }

    int getAvailablePort(int port) {
        int newPort = port;
        try (var socket = new ServerSocket(newPort)) {
            // try to start socket with provided port
        } catch (IllegalArgumentException e) {
            newPort = Integer.parseInt(DEFAULT_PORT);
            newPort = getAvailablePort(newPort);
        } catch (Exception e) {
            newPort = getAvailablePort(newPort + 1);
        }
        return newPort;
    }

    void isEligibleArgsNumber(String... args) {
        if (args.length > 2) {
            throw new RuntimeException("Incorrect arguments number, max number of arguments is 2:" +
                    " arg_1: port, arg_2: path to a property file or both args simultaneously.\nAn order is not sensitive");
        }
    }

    public int getPort() {
        return port;
    }

    public Properties getProperties() {
        return properties;
    }
}
