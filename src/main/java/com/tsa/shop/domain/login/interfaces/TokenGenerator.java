package com.tsa.shop.domain.login.interfaces;

import java.util.UUID;

public interface TokenGenerator {
    UUID generate();
}
