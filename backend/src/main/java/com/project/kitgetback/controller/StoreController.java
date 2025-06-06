package com.project.kitgetback.controller;

import com.project.kitgetback.DTO.Product;
import com.project.kitgetback.DTO.ProductRequest;
import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.entity.ProductEntity;
import com.project.kitgetback.repository.MembershipRepository;
import com.project.kitgetback.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://im-uisoon.github.io")
public class StoreController {
    private final StoreService storeService;
    private final MembershipRepository membershipRepository;

    public StoreController(StoreService storeService, MembershipRepository membershipRepository) {
        this.storeService = storeService;
        this.membershipRepository = membershipRepository;
    }

    // 해당 학교 강의 조회
    @GetMapping("/{schoolCode}/classes")
    public List<String> getClasses(@PathVariable String schoolCode) {
        return storeService.getDistinctClassesBySchool(schoolCode);
    }

    // 해당 학교 강의 준비물 조회
    @GetMapping("{schoolCode}/{className}/items")
    public List<Product> getProducts(@PathVariable String schoolCode, @PathVariable String className) {
        return storeService.getProductsBySchoolAndClass(schoolCode, className);
    }

    // 상품 추가
    @PostMapping("/register")
    public ResponseEntity<?> registerProduct(@RequestBody ProductRequest req, Authentication authentication) {
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"));
        String school = member.getSchool();

        ProductEntity product = new ProductEntity();
        product.setSchool(school);
        product.setClassName(req.className());
        product.setProductName(req.productName());
        product.setPrice(req.price());

        ProductEntity saved = storeService.saveProduct(product);
        return ResponseEntity.ok(saved);
    }
}