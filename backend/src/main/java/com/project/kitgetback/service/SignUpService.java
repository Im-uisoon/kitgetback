package com.project.kitgetback.service;

import com.project.kitgetback.DTO.SignUpRequest;
import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.repository.MembershipRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    private final MembershipRepository membershipRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public SignUpService(MembershipRepository membershipRepository, BCryptPasswordEncoder passwordEncoder) {
        this.membershipRepository = membershipRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(SignUpRequest request) {
        if (membershipRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일 입니다");
        }

        MembershipEntity member = new MembershipEntity();
        member.setEmail(request.email());
        member.setPassword(passwordEncoder.encode(request.password()));
        member.setSchool(request.school());
        member.setName(request.name());
        member.setCode(request.code());
        member.setAuth(request.auth());
        membershipRepository.save(member);
    }
}
