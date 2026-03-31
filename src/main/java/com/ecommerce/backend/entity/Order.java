package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Product> products;

    private double totalPrice;

    private String status;
}