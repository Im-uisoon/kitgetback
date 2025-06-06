package com.project.kitgetback.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "order_list")
public class OrderListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode;            // 학번
    private String userName;            // 이름
    private LocalDateTime createdAt;    // 주문 시간
    private String cardNumber;
    private String payment;
    private String shipping;
    private String postplace;

    // 주문 상세 리스트
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems;
}
