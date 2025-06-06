package com.project.kitgetback.controller;

import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.entity.NoticeEntity;
import com.project.kitgetback.repository.MembershipRepository;
import com.project.kitgetback.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
@CrossOrigin(origins = "https://im-uisoon.github.io")
public class NoticeController {
    private final NoticeService noticeService;
    private final MembershipRepository membershipRepository;

    public NoticeController(NoticeService noticeService, MembershipRepository membershipRepository) {
        this.noticeService = noticeService;
        this.membershipRepository = membershipRepository;
    }

    // 목록 (페이지네이션)
    @GetMapping
    public Page<NoticeEntity> getNotices(@RequestParam(defaultValue =  "0") int page) {
        return  noticeService.getNotices(page, 5);
    }

    // 상세 페이지
    @GetMapping("/{id}")
    public NoticeEntity getNotice(@PathVariable Long id) { return noticeService.getNotice(id); }

    // 등록(교사 권한)
    @PostMapping
    public NoticeEntity createNotice(@RequestBody NoticeRequest req, Authentication authentication) {
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"));
        if (!"teacher".equals(member.getAuth())) throw new RuntimeException("권한이 없습니다");
        return noticeService.createNotice(email, member.getName(), member.getAuth(), req.title(), req.content());
    }

    // 글 삭제 (교사권한 + 본인 글만 삭제)
    @DeleteMapping("/{id}")
    public void deleteNotice(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"));
        noticeService.deleteNotice(id, email, member.getAuth());
    }

    public record NoticeRequest(String title, String content) {}
}
