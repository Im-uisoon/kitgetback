package com.project.kitgetback.controller;

import com.project.kitgetback.DTO.OrderRequest;
import com.project.kitgetback.DTO.Order;
import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.repository.MembershipRepository;
import com.project.kitgetback.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://im-uisoon.github.io")
public class OrderController {
    private final OrderService orderService;
    private final MembershipRepository membershipRepository;

    public OrderController(OrderService orderService, MembershipRepository membershipRepository) {
        this.orderService = orderService;
        this.membershipRepository = membershipRepository;
    }

    // 주문 등록
    @PostMapping("/order")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        orderService.addOrder(req, email);
        return ResponseEntity.ok().build();
    }

    // 주문 내역 조회
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrderSummaries(Authentication authentication) {
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다"));
        String userCode = member.getCode();
        List<Order> orders = orderService.getOrderSummariesByUserCode(userCode);
        return ResponseEntity.ok(orders);
    }

    // 주문 취소
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, Authentication authentication) {
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다"));
        String userCode = member.getCode();
        orderService.deleteOrder(orderId, userCode);
        return ResponseEntity.noContent().build();
    }
}
