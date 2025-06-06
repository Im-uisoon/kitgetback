package com.project.kitgetback.repository;

import com.project.kitgetback.DTO.Product;
import com.project.kitgetback.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    // 학교 코드 찾기
    @Query("SELECT DISTINCT s.className FROM ProductEntity s WHERE s.school = :schoolCode")
    List<String> findDistinctClassesBySchool(@Param("schoolCode") String schoolCode);

    // 학교의 수업 코드 찾기
    @Query("SELECT new com.project.kitgetback.DTO.Product(s.productName, s.price) FROM ProductEntity s WHERE s.school = :schoolCode AND s.className = :className")
    List<Product> findProductsBySchoolAndClass(@Param("schoolCode") String schoolCode, @Param("className") String className);
}
