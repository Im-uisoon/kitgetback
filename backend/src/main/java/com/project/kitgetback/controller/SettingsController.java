package com.project.kitgetback.controller;

import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.entity.OrderListEntity;
import com.project.kitgetback.repository.MembershipRepository;
import com.project.kitgetback.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://im-uisoon.github.io")
public class SettingsController {
    private final BCryptPasswordEncoder passwordEncoder;
    private final MembershipRepository membershipRepository;
    private final OrderRepository orderRepository;

    public SettingsController(
            BCryptPasswordEncoder passwordEncoder,
            MembershipRepository membershipRepository,
            OrderRepository orderRepository) {
        this.passwordEncoder = passwordEncoder;
        this.membershipRepository = membershipRepository;
        this.orderRepository = orderRepository;
    }

    // 비밀번호 확인
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordRequest req, Authentication authentication) {
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"));
        if (!passwordEncoder.matches(req.password(), member.getPassword())) {
            return ResponseEntity.status(401).body("비밀번호가 일치하지 않습니다");
        }
        return ResponseEntity.ok().build();
    }

    // 비밀번호 변경
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req, Authentication authentication) {
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"));
        member.setPassword(passwordEncoder.encode(req.newPassword()));
        membershipRepository.save(member);
        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(Authentication authentication, HttpServletRequest request) {
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"));
        String userCode = member.getCode();

        // 주문 내역 삭제 (order_list, order_item)
        List<OrderListEntity> orders = orderRepository.findByUserCode(userCode);
        orderRepository.deleteAll(orders);

        // 회원 정보 삭제
        membershipRepository.delete(member);

        // 세션 / 인증 만료
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }

    public record ChangePasswordRequest(String newPassword) {}
    record PasswordRequest(String password) {}
}
