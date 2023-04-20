package com.tsa.shop.domain.argsparser.interfaces;

import com.tsa.shop.domain.argsparser.enums.Property;

import java.io.Serializable;

public interface EnvironmentVariablesContext {
    Serializable getProperty(Property property);

    void setProperty(String tag, Serializable content);
}
