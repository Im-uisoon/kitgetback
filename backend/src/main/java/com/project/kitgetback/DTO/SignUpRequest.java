package com.project.kitgetback.DTO;

public record SignUpRequest(
        String email,
        String password,
        String school,
        String name,
        String code,
        String auth
) {}
