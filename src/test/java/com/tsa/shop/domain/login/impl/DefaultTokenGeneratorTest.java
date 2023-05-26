package com.tsa.shop.domain.login.impl;

import com.tsa.shop.domain.login.interfaces.TokenGenerator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DefaultTokenGeneratorTest {

    @Test
    void shouldReturnUUIInstance() {
        TokenGenerator tokenGenerator = new DefaultTokenGenerator();

        UUID result = tokenGenerator.generate();
        assertNotNull(result);
        assertFalse(result.toString().isEmpty());
    }
}