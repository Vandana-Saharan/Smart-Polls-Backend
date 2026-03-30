package com.smartpolls.smartpollsapi.service;

import com.smartpolls.smartpollsapi.dto.AuthResponse;
import com.smartpolls.smartpollsapi.dto.LoginRequest;
import com.smartpolls.smartpollsapi.dto.MessageResponse;
import com.smartpolls.smartpollsapi.dto.RegisterRequest;
import com.smartpolls.smartpollsapi.entity.AppUser;
import com.smartpolls.smartpollsapi.entity.UserRole;
import com.smartpolls.smartpollsapi.exception.InvalidCredentialsException;
import com.smartpolls.smartpollsapi.exception.UsernameAlreadyExistsException;
import com.smartpolls.smartpollsapi.repository.AppUserRepository;
import com.smartpolls.smartpollsapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Value("${security.admin-usernames:admin}")
    private String adminUsernames;

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException();
        }

        UserRole role = isAdminUsername(request.username()) ? UserRole.ADMIN : UserRole.USER;
        AppUser user = new AppUser(
                UUID.randomUUID(),
                request.username(),
                passwordEncoder.encode(request.password()),
                Instant.now(),
                role
        );
        userRepository.save(user);
        return new MessageResponse("User registered successfully");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(InvalidCredentialsException::new);

        boolean matches = passwordEncoder.matches(request.password(), user.getPasswordHash());
        if (!matches) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getRole(), "Login successful");
    }

    private boolean isAdminUsername(String username) {
        Set<String> configuredAdmins = Arrays.stream(adminUsernames.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(value -> value.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
        return configuredAdmins.contains(username.toLowerCase(Locale.ROOT));
    }
}
