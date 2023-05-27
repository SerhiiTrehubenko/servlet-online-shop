package com.tsa.shop.argsparser.impl;

import com.tsa.shop.argsparser.enums.Property;
import com.tsa.shop.argsparser.interfaces.EnvironmentVariablesContext;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultEnvironmentVariablesContext implements EnvironmentVariablesContext {

    private final Map<String, Serializable> properties;
    public DefaultEnvironmentVariablesContext(Property... property) {
        properties = Arrays.stream(property)
                .collect(Collectors.toMap(Property::getTag, Property::getProperty));
    }

    @Override
    public Serializable getProperty(Property property) {
        return properties.get(property.getTag());
    }

    @Override
    public void setProperty(String tag, Serializable content) {
            properties.put(tag, content);
    }
}
