package com.project.kitgetback.service;

import com.project.kitgetback.repository.DirectoryRepository;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {
    private final DirectoryRepository directoryRepository;

    public VerificationService(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    public boolean verifyMember(String school, String name, String code) {
        return directoryRepository.existsBySchoolAndNameAndCode(school, name, code);
    }
}
