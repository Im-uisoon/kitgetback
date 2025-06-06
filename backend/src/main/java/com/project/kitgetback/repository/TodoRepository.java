package com.project.kitgetback.repository;

import com.project.kitgetback.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    // 이메일로 조회
    List<TodoEntity> findByEmail(String email);
}
