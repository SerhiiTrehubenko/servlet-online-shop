package com.tsa.shop.domain.argsparser.interfaces;

public interface ArgsParser {

    EnvironmentVariablesContext getContext();

    void parse(String... incomeArgs);
}
