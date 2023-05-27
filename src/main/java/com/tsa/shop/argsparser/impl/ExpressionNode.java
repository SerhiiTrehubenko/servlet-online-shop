package com.tsa.shop.argsparser.impl;

import com.tsa.shop.argsparser.interfaces.EnvironmentVariablesContext;
import com.tsa.shop.argsparser.interfaces.Expression;

import java.io.Serializable;
import java.util.Objects;

public class ExpressionNode implements Expression {
    private final String tag;
    private final Serializable content;

    private Expression nextNode;

    public ExpressionNode(String tag, Serializable content) {
        this.tag = tag;
        this.content = content;
    }

    @Override
    public void interpret(EnvironmentVariablesContext content) {
        content.setProperty(tag, this.content);
        if (Objects.nonNull(nextNode)) {
            nextNode.interpret(content);
        }
    }

    @Override
    public void setNextNode(Expression nextNode) {
        this.nextNode = nextNode;
    }
}
