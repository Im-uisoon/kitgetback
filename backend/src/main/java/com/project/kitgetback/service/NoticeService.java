package com.project.kitgetback.service;

import com.project.kitgetback.entity.NoticeEntity;
import com.project.kitgetback.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public Page<NoticeEntity> getNotices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public NoticeEntity getNotice(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지입니다"));
    }

    public NoticeEntity createNotice(String email, String name, String auth, String title, String content) {
        NoticeEntity notice = new NoticeEntity();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setWriterEmail(email);
        notice.setWriterName(name);
        notice.setWriterAuth(auth);
        notice.setCreatedAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    public void deleteNotice(Long id, String email, String auth) {
        NoticeEntity notice = getNotice(id);
        if (!auth.equals("teacher")) throw new RuntimeException("권한이 없습니다");
        if (!notice.getWriterEmail().equals(email)) throw new RuntimeException("본인 글만 삭제할 수 있습니다");
        noticeRepository.delete(notice);
    }
}
