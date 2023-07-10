package com.tsa.shop.cart;

import com.tsa.shop.domain.*;
import com.tsa.shop.login.repo.TokenRepository;

import java.util.List;
import java.util.Map;

public class CartService {
    public final CartRepository cartRepository;
    private final TokenRepository tokenRepository;
    private final DtoExtractor extractor;

    public CartService(CartRepository cartRepository,
                       TokenRepository tokenRepository,
                       DtoExtractor extractor) {
        this.cartRepository = cartRepository;
        this.tokenRepository = tokenRepository;
        this.extractor = extractor;
    }

    public void addToCart(Map<String, String[]> parameters, String token) {
        ProductDto productDto = extractor.getFullDtoInstanceFrom(parameters);
        Session session = tokenRepository.getSession(token);

        if (cartRepository.isPresent(session.user())) {
            Cart existCart = cartRepository.getCart(session.user());
            existCart.add(productDto);
        } else {
            Cart newCart = new Cart();
            newCart.add(productDto);
            cartRepository.addCart(session.user(), newCart);
        }
    }

    public List<ProductDto> getAllProducts(String token) {
        Session session = tokenRepository.getSession(token);
        if (cartRepository.isPresent(session.user())) {
            Cart cart = cartRepository.getCart(session.user());
            return cart.getAll();
        }
        throw new WebServerException("Your cart is empty", HttpStatus.NOT_FOUND, this);
    }

}
