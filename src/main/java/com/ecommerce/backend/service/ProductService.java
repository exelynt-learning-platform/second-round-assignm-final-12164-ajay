package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public Product save(Product p) { return repo.save(p); }
    public List<Product> getAll() { return repo.findAll(); }
    public void delete(Long id) { repo.deleteById(id); }
}