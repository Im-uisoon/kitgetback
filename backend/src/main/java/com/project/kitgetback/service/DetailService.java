package com.project.kitgetback.service;

import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.repository.MembershipRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DetailService implements UserDetailsService {
    private final MembershipRepository membershipRepository;

    public DetailService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MembershipEntity member = membershipRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email:"+email));
        return User.withUsername(member.getEmail())
                .password(member.getPassword())
                .roles("USER")
                .build();
    }
}
