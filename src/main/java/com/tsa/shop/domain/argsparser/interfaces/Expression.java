package com.tsa.shop.domain.argsparser.interfaces;

public interface Expression {
    void interpret(EnvironmentVariablesContext context);

    void setNextNode(Expression nextNode);
}
