package com.tsa.shop.login.impl;

import com.tsa.shop.domain.Session;
import com.tsa.shop.domain.User;
import com.tsa.shop.login.repoimpl.DefaultTokenRepository;
import com.tsa.shop.login.repo.TokenRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DefaultTokenRepositoryTest {
    @Test
    void shouldAddTokenToRepository() {
        UUID expectedToken = UUID.randomUUID();
        String tokenAsString = expectedToken.toString();

        TokenRepository tokenRepository = new DefaultTokenRepository();

        tokenRepository.add(new Session(mock(User.class), expectedToken, System.currentTimeMillis() + 36000));

        assertTrue(tokenRepository.isPresent(tokenAsString));
    }
}