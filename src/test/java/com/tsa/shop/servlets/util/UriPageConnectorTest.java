package com.tsa.shop.servlets.util;

import com.tsa.shop.servlets.enums.UriPageConnector;
import org.junit.jupiter.api.Test;

class UriPageConnectorTest {
    //TODO: finish tests of UriPageConnector
    @Test
    void name() {
        for (UriPageConnector value : UriPageConnector.values()) {
            System.out.println(value.getUri());
            System.out.println(value.getHtmlPage());
        }
    }

}