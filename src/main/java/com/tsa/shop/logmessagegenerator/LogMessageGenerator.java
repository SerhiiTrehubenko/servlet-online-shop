package com.tsa.shop.logmessagegenerator;

import com.tsa.shop.exceptions.WebServerException;

public interface LogMessageGenerator {
    String getMessageFrom(WebServerException exception);

    String getMessageFrom(RuntimeException exception);
}
