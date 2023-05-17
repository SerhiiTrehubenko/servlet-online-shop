package com.tsa.shop.domain.logmessagegenerator;

import com.tsa.shop.servlets.exceptions.WebServerException;

public interface LogMessageGenerator {
    String getMessageFrom(WebServerException exception);

    String getMessageFrom(RuntimeException exception);
}
