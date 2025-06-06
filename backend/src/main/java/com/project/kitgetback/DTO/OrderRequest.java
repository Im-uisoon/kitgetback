package com.project.kitgetback.DTO;

import java.util.List;

public record OrderRequest(
        String cardNumber,
        String payment,
        String shipping,
        String postplace,
        List<OrderItem> items
) {
}
