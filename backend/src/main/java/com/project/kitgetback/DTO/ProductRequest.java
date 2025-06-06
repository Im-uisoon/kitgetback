package com.project.kitgetback.DTO;

public record ProductRequest(
        String className,
        String productName,
        int price
) {}
