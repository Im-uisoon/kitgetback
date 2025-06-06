package com.project.kitgetback.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long orderId,
        String userCode,
        String userName,
        LocalDateTime orderDate,
        List<String> itemNames,
        int totalPrice,
        String payment,
        String shipping,
        String postplace
) {}
