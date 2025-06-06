package com.project.kitgetback.service;

import com.project.kitgetback.DTO.OrderItem;
import com.project.kitgetback.DTO.OrderRequest;
import com.project.kitgetback.DTO.Order;
import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.entity.OrderItemEntity;
import com.project.kitgetback.entity.OrderListEntity;
import com.project.kitgetback.repository.MembershipRepository;
import com.project.kitgetback.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MembershipRepository membershipRepository;

    public OrderService(OrderRepository orderRepository, MembershipRepository membershipRepository) {
        this.orderRepository = orderRepository;
        this.membershipRepository = membershipRepository;
    }

    // 주문 등록
    @Transactional
    public void addOrder(OrderRequest req, String email) {
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다"));

        OrderListEntity order = new OrderListEntity();
        order.setUserCode(member.getCode());
        order.setUserName(member.getName());
        order.setCreatedAt(LocalDateTime.now());
        order.setCardNumber(req.cardNumber());
        order.setPayment(req.payment());
        order.setShipping(req.shipping());
        order.setPostplace(req.postplace());

        List<OrderItemEntity> itemEntities = new ArrayList<>();
        for (OrderItem itemDto : req.items()) {
            OrderItemEntity item = new OrderItemEntity();
            item.setProductName(itemDto.productName());
            item.setPrice(itemDto.price());
            item.setOrder(order);
            itemEntities.add(item);
        }
        order.setOrderItems(itemEntities);

        orderRepository.save(order);
    }

    // 주문 내역 조회
    @Transactional
    public List<Order> getOrderSummariesByUserCode(String userCode) {
        List<OrderListEntity> orders = orderRepository.findByUserCode(userCode);
        return orders.stream()
                .map(order -> new Order(
                        order.getId(),
                        order.getUserCode(),
                        order.getUserName(),
                        order.getCreatedAt(),
                        order.getOrderItems().stream()
                                .map(OrderItemEntity::getProductName)
                                .collect(Collectors.toList()),
                        order.getOrderItems().stream()
                                .mapToInt(OrderItemEntity::getPrice)
                                .sum(),
                        order.getPayment(),
                        order.getShipping(),
                        order.getPostplace()
                ))
                .collect(Collectors.toList());
    }

    // 주문 취소
    @Transactional
    public void deleteOrder(Long orderId, String userCode) {
        OrderListEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문이 없습니다"));
        if (!order.getUserCode().equals(userCode)) {
            throw new RuntimeException("본인 주문만 취소 가능합니다");
        }
        orderRepository.delete(order);
    }
}
