package com.tsa.shop.login.impl;

import com.tsa.shop.login.interfaces.UserRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    @Override
    public User getUser(String email) {
        return users.get(email);
    }

    @Override
    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }
}
