package com.tsa.shop.logmessagegenerator;

import com.tsa.shop.domain.WebServerException;

public interface LogMessageGenerator {
    String getMessageFrom(WebServerException exception);

    String getMessageFrom(RuntimeException exception);
}
