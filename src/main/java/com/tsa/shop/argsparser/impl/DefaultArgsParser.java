package com.tsa.shop.argsparser.impl;

import com.tsa.shop.argsparser.enums.Property;
import com.tsa.shop.argsparser.interfaces.ArgsParser;
import com.tsa.shop.argsparser.interfaces.EnvironmentVariablesContext;
import com.tsa.shop.argsparser.interfaces.Expression;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class DefaultArgsParser implements ArgsParser {

    private final static int EMPTY_ARGS = 0;
    private final EnvironmentVariablesContext context;

    private Expression expressionNode;

    private ListIterator<String> argsIterator;

    public DefaultArgsParser(EnvironmentVariablesContext context) {
        this.context = context;
    }

    @Override
    public void parse(String... incomeArgs) {
        if (nonEmpty(incomeArgs)) {
            setArgsIterator(incomeArgs);
            buildInterpretTree();
            updateContext();
            deleteInterpretTree();
        }
    }

    private boolean nonEmpty(String... incomeArgs) {
        return incomeArgs.length != EMPTY_ARGS;
    }

    private void setArgsIterator(String... incomeArgs) {
        List<String> splitArgs = Arrays.asList(incomeArgs);
        argsIterator = splitArgs.listIterator();
    }

    private void buildInterpretTree() {
        while (argsIterator.hasNext()) {
            createNode();
        }
    }

    private void createNode() {
        Expression currentNode = resolveNode();
        chainNode(currentNode);
    }

    Expression resolveNode() {
        Property propertyType = resolveProperty();
        return propertyType.createExpressionNode(getArgValue(propertyType));
    }

    String getArgValue(Property propertyType) {
        try {
            return argsIterator.next();
        } catch (Exception e) {
            throw new RuntimeException("Provided arg tag: [%s] does not have a value".formatted(propertyType.getTag()));
        }
    }

    private Property resolveProperty() {
        String incomeTag = argsIterator.next();
        for (Property defaultProperty : Property.values()) {
            if (defaultProperty.getTag().equals(incomeTag)) {
                return defaultProperty;
            }
        }
        throw new RuntimeException("The Provided tad: [%s] does not comply to incomeTag scheme".formatted(incomeTag));
    }

    private void chainNode(Expression currentNode) {
        currentNode.setNextNode(expressionNode);
        expressionNode = currentNode;
    }

    private void updateContext() {
        expressionNode.interpret(context);
    }

    private void deleteInterpretTree() {
        expressionNode = null;
    }

    @Override
    public EnvironmentVariablesContext getContext() {
        return context;
    }
}
