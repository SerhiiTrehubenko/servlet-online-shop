package com.tsa.shop.login.repo;

import com.tsa.shop.domain.User;

public interface UserRepository {
    User getUser(String name);
    void addUser(User user);
}
