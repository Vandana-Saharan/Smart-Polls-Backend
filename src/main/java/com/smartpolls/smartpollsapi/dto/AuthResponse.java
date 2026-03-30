package com.smartpolls.smartpollsapi.dto;

import com.smartpolls.smartpollsapi.entity.UserRole;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID userId,
        String username,
        UserRole role,
        String message
) {
}
