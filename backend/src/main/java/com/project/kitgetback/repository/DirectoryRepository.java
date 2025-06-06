package com.project.kitgetback.repository;

import com.project.kitgetback.entity.DirectoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectoryRepository extends JpaRepository<DirectoryEntity, Integer> {
    // 회원가입 전 인증 -> 학교,이름,학번
    boolean existsBySchoolAndNameAndCode(String school, String name, String code);
}
