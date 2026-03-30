package com.smartpolls.smartpollsapi.security;

import com.smartpolls.smartpollsapi.entity.UserRole;

import java.io.Serializable;
import java.util.UUID;

/**
 * Minimal principal stored in Spring Security context for JWT-authenticated users.
 */
public record JwtUserPrincipal(UUID userId, String username, UserRole role) implements Serializable {

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
}
