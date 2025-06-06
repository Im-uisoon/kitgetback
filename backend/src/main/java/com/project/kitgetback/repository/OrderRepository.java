package com.project.kitgetback.repository;

import com.project.kitgetback.entity.OrderListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderListEntity, Long> {
    List<OrderListEntity> findByUserCode(String userCode);
}
