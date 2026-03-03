package com.example.minimall.service;

import com.example.minimall.entity.Product;
import com.example.minimall.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepo;
    public ProductService(ProductRepository productRepo) { this.productRepo = productRepo; }

    public List<Product> listAll(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return productRepo.findAll();
        }
        return productRepo.findByNameContainingIgnoreCase(keyword);
    }

    public Product getById(Long id) {
        return productRepo.findById(id).orElse(null);
    }
}
