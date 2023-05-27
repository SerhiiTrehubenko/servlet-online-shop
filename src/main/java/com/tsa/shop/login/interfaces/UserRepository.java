package com.tsa.shop.login.interfaces;

import com.tsa.shop.login.impl.User;

public interface UserRepository {
    User getUser(String name);
    void addUser(User user);
}
