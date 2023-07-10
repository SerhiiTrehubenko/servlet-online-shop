package com.tsa.shop.domain;

import java.util.UUID;

public record Session(User user,
        UUID token,
        long expireTime) { }
