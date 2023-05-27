package com.tsa.shop.argsparser.interfaces;

import com.tsa.shop.argsparser.enums.Property;

import java.io.Serializable;

public interface EnvironmentVariablesContext {
    Serializable getProperty(Property property);

    void setProperty(String tag, Serializable content);
}
