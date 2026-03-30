package com.smartpolls.smartpollsapi.controller;

import com.smartpolls.smartpollsapi.dto.AuthResponse;
import com.smartpolls.smartpollsapi.dto.LoginRequest;
import com.smartpolls.smartpollsapi.dto.MessageResponse;
import com.smartpolls.smartpollsapi.dto.MeResponse;
import com.smartpolls.smartpollsapi.dto.RegisterRequest;
import com.smartpolls.smartpollsapi.entity.AppUser;
import com.smartpolls.smartpollsapi.exception.UnauthorizedException;
import com.smartpolls.smartpollsapi.repository.AppUserRepository;
import com.smartpolls.smartpollsapi.security.JwtUserPrincipal;
import com.smartpolls.smartpollsapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppUserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        JwtUserPrincipal jp = requirePrincipal(authentication);
        UUID userId = jp.userId();
        AppUser user = userRepository.findById(userId).orElseThrow();
        return new MeResponse(user.getId(), user.getUsername(), user.getRole());
    }

    private JwtUserPrincipal requirePrincipal(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUserPrincipal principal)) {
            throw new UnauthorizedException();
        }
        return principal;
    }
}
