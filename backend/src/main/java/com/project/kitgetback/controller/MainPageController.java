package com.project.kitgetback.controller;

import com.project.kitgetback.DTO.SignUpRequest;
import com.project.kitgetback.DTO.VerificationRequest;
import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.repository.MembershipRepository;
import com.project.kitgetback.service.SignUpService;
import com.project.kitgetback.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://im-uisoon.github.io")
public class MainPageController {
    private final AuthenticationManager authenticationManager;
    private final VerificationService verificationService;
    private final SignUpService signUpService;
    private final MembershipRepository membershipRepository;

    public MainPageController(
            AuthenticationManager authenticationManager,
            VerificationService verificationService,
            SignUpService signUpService,
            MembershipRepository membershipRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.verificationService = verificationService;
        this.signUpService = signUpService;
        this.membershipRepository = membershipRepository;
    }

    // 회원가입 전 인증
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerificationRequest request) {
        boolean verified = verificationService.verifyMember(
                request.school(),
                request.name(),
                request.code()
        );
        return ResponseEntity.ok(new VerificationResponse(verified));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            signUpService.signUp(request);
            return ResponseEntity.ok(new SignUpResponse("가입 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new SignUpResponse(e.getMessage()));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new LoginResponse("로그인 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new LoginResponse("이메일/비밀번호 확인해주세요"));
        }
    }

    // 로그인 상태 확인
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.ok(new AuthResponse(true, auth.getName()));
        }
        return ResponseEntity.ok(new AuthResponse(false, null));
    }

    // 로그인 유저 프로필 확인
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body("다시 로그인해주세요");
        }
        String email = authentication.getName();
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"));
        return ResponseEntity.ok(new MyInfoResponse(member.getEmail(), member.getName(), member.getAuth()));
    }

    public record MyInfoResponse(String email, String name, String auth) {}
    record VerificationResponse(boolean verified) {}
    record SignUpResponse(String message) {}
    record LoginResponse(String message) {}
    record LoginRequest(String email, String password) {}
    record AuthResponse(boolean isAuthenticated, String email) {}
}
