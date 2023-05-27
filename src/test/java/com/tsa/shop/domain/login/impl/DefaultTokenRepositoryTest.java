package com.tsa.shop.domain.login.impl;

import com.tsa.shop.login.impl.DefaultTokenRepository;
import com.tsa.shop.login.interfaces.TokenRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultTokenRepositoryTest {
    @Test
    void shouldAddTokenToRepository() {
        UUID expectedToken = UUID.randomUUID();
        String tokenAsString = expectedToken.toString();

        TokenRepository tokenRepository = new DefaultTokenRepository();

        tokenRepository.add(expectedToken);

        assertTrue(tokenRepository.isPresent(tokenAsString));
    }
}