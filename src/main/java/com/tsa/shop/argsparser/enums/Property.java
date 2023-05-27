package com.tsa.shop.argsparser.enums;

import com.tsa.shop.argsparser.impl.ExpressionNode;
import com.tsa.shop.argsparser.interfaces.Expression;

import java.io.Serializable;
import java.util.Objects;

public enum Property {
    PORT("-p", 3001) {
        @Override
        public Serializable getProperty(Serializable parameter) {
            try {
                return Integer.parseInt(parameter.toString());
            } catch (Exception e) {
                throw new RuntimeException("Provided port value: [%s] is not an integer".formatted(parameter));
            }
        }
    },

    FILE_PROPERTY("-f", "application.properties") {
        @Override
        public Serializable getProperty() {
            return getProperty(property);
        }

        @Override
        public Serializable getProperty(Serializable parameter) {
            try (var input = Property.class
                    .getClassLoader()
                    .getResourceAsStream(Objects.toString(parameter))) {
                java.util.Properties properties = new java.util.Properties();
                properties.load(input);
                return properties;
            } catch (Exception e) {
                throw new RuntimeException("The is no an [application.property] file by provided path [%s]\n CAUSE: %s"
                        .formatted(Objects.toString(parameter), e.toString()));
            }
        }
    };

    private final String tag;
    protected final Serializable property;

    Property(String tag, Serializable property) {
        this.tag = tag;
        this.property = property;
    }

    public Serializable getProperty() {
        return property;
    }

    protected abstract Serializable getProperty(Serializable parameter);

    public String getTag() {
        return tag;
    }

    public Expression createExpressionNode(String content) {
        return new ExpressionNode(tag, getProperty(content));
    }
}
