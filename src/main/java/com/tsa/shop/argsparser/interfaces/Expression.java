package com.tsa.shop.argsparser.interfaces;

public interface Expression {
    void interpret(EnvironmentVariablesContext context);

    void setNextNode(Expression nextNode);
}
