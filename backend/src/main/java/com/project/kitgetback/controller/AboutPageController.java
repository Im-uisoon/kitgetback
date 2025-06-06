package com.project.kitgetback.controller;

import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.repository.MembershipRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://im-uisoon.github.io")
public class AboutPageController {
    private final MembershipRepository membershipRepository;

    public AboutPageController(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    // 사용자 school 찾기
    @GetMapping("/user/school")
    public ResponseEntity<?> getUserSchool() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            MembershipEntity member = membershipRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저" + email));
            return ResponseEntity.ok(new SchoolResponse(member.getSchool()));
        }
        return ResponseEntity.status(401).body(new ErrorResponse("인증되지 않은 사용자입니다"));
    }

    // 진짜 이 학교 소속이 맞나 한번 더 확인
    @GetMapping("/school/{schoolCode}")
    public ResponseEntity<?> checkSchoolAccess(@PathVariable String schoolCode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            MembershipEntity member = membershipRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저" + email));
            if (member.getSchool().equals(schoolCode)) {
                return ResponseEntity.ok(new AccessResponse(true));
            }
            return ResponseEntity.status(403).body(new AccessResponse(false));
        }
        return ResponseEntity.status(401).body(new ErrorResponse("인증되지 않은 사용자입니다"));
    }

    record SchoolResponse(String school) {}
    record ErrorResponse(String message) {}
    record AccessResponse(boolean isAccess) {}
}
