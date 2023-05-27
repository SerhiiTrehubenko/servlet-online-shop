package com.tsa.shop.login.impl;

import com.tsa.shop.login.interfaces.IncomeDataProvider;

import java.util.Map;

public class IncomeDataProviderImpl implements IncomeDataProvider {

    private final static String NAME_PARAMETER = "email";
    private final static String PASSWORD_PARAMETER = "password";
    private final static int VALUE = 0;


    private final String name;
    private final String password;

    public IncomeDataProviderImpl(Map<String, String[]> parameters) {
        name = parameters.get(NAME_PARAMETER)[VALUE];
        password = parameters.get(PASSWORD_PARAMETER)[VALUE];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
