package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Cart;
import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public Order createOrder(User user, String shippingDetails) throws StripeException {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setProducts(cart.getItems().stream()
                .map(CartItem::getProduct)
                .collect(Collectors.toList()));
        order.setTotalPrice(totalPrice);
        order.setShippingDetails(shippingDetails);
        order.setPaymentStatus("PENDING");

        // Stripe payment
        Stripe.apiKey = stripeApiKey;
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(totalPrice.multiply(BigDecimal.valueOf(100)).longValue())
                .setCurrency("usd")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        if ("succeeded".equals(paymentIntent.getStatus())) {
            order.setPaymentStatus("PAID");
        } else {
            order.setPaymentStatus("FAILED");
        }

        orderRepository.save(order);

        // Clear cart after order
        cart.getItems().clear();
        cartRepository.save(cart);

        return order;
    }
}