package com.tsa.shop.cart;

import com.tsa.shop.domain.User;

import java.util.concurrent.ConcurrentHashMap;

public class CartRepository {
    private final ConcurrentHashMap<User, Cart> carts = new ConcurrentHashMap<>();

    public boolean isPresent(User user) {
        return carts.containsKey(user);
    }

    public void addCart(User user, Cart cart) {
        carts.put(user, cart);
    }

    public Cart getCart(User user) {
        return carts.get(user);
    }
}
