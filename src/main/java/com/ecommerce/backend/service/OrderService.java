package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;

    public Order createOrder(User user) {

        Cart cart = cartRepo.findByUser(user).orElseThrow();

        Order order = new Order();
        order.setUser(user);
        order.setProducts(cart.getProducts());

        double total = cart.getProducts()
                .stream()
                .mapToDouble(Product::getPrice)
                .sum();

        order.setTotalPrice(total);
        order.setStatus("CREATED");

        return orderRepo.save(order);
    }
}