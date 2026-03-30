package com.smartpolls.smartpollsapi.dto;

import com.smartpolls.smartpollsapi.entity.UserRole;

import java.util.UUID;

public record MeResponse(
        UUID userId,
        String username,
        UserRole role
) {
}
