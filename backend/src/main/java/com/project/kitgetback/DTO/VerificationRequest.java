package com.project.kitgetback.DTO;

public record VerificationRequest(
        String school,
        String name,
        String code
) {
}
