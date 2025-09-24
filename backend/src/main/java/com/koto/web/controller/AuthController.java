package com.koto.web.controller;

import com.koto.domain.AppUser;
import com.koto.repository.AppUserRepository;
import com.koto.service.AuthService;
import com.koto.web.dto.auth.AuthResponse;
import com.koto.web.dto.auth.LoginRequest;
import com.koto.web.dto.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {
    private final AuthService authService;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        AppUser user = authService.register(req.email(), req.usernameGlobal(), req.password());
        String token = authService.issueToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), user.getUsernameGlobal()));
    }

    @Operation(summary = "Login and receive JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        // Authenticate using AuthenticationManager to leverage PasswordEncoder
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        Optional<AppUser> userOpt = userRepository.findByEmail(req.email());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        AppUser user = userOpt.get();
        String token = authService.issueToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), user.getUsernameGlobal()));
    }
}

