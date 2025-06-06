package com.project.kitgetback.service;

import com.project.kitgetback.DTO.Product;
import com.project.kitgetback.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    @Autowired
    private ProductRepository productRepository;

    public List<String> getDistinctClassesBySchool(String schoolCode) {
        return productRepository.findDistinctClassesBySchool(schoolCode);
    }

    public List<Product> getProductsBySchoolAndClass(String schoolCode, String className) {
        return productRepository.findProductsBySchoolAndClass(schoolCode, className);
    }

    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }
}
