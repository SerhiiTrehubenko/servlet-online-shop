package com.tsa.shop.argsparser.interfaces;

public interface ArgsParser {

    EnvironmentVariablesContext getContext();

    void parse(String... incomeArgs);
}
