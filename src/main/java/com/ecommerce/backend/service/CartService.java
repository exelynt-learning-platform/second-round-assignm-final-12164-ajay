package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public Cart addToCart(User user, Long productId) {

        Cart cart = cartRepo.findByUser(user)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    return c;
                });

        cart.getProducts().add(productRepo.findById(productId).orElseThrow());

        return cartRepo.save(cart);
    }
}