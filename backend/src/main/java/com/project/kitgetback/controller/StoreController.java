package com.project.kitgetback.controller;

import com.project.kitgetback.DTO.Product;
import com.project.kitgetback.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://im-uisoon.github.io")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
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
}