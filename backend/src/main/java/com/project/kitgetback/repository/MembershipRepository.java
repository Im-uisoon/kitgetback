package com.project.kitgetback.repository;

import com.project.kitgetback.entity.MembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<MembershipEntity, Integer> {
    // 회원가입 중복 검사
    boolean existsByEmail(String email);

    // 회원 정보 조회
    Optional<MembershipEntity> findByEmail(String email);
}
