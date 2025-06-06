package com.project.kitgetback.repository;

import com.project.kitgetback.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    Page<NoticeEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
