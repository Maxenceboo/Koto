package com.koto.web.controller;

import com.koto.domain.AppUser;
import com.koto.service.AppUserService;
import com.koto.web.dto.appuser.AppUserDto;
import com.koto.web.mapper.AppUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "AppUser", description = "Operations related to application users")
public class AppUserController {
    private final AppUserService service;

    @Operation(summary = "Lister tous les utilisateurs")
    @GetMapping
    public List<AppUserDto> getAllUsers() {
        return service.list().stream()
                .map(AppUserMapper::toDto)
                .toList();
    }

    @Operation(summary = "créer un utilisateur")
    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@Valid @RequestBody AppUser user) {
        AppUser createdUser = service.create(user.getEmail(), user.getUsernameGlobal());
        return ResponseEntity.created(URI.create("/api/users/" + createdUser.getId()))
                .body(AppUserMapper.toDto(createdUser));
    }

    @Operation(summary = "Mettre à jour un utilisateur par ID")
    @PutMapping("/{id}")
    public ResponseEntity<AppUserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody AppUser user) {
        AppUser updatedUser = service.update(id, user);
        return ResponseEntity.ok(AppUserMapper.toDto(updatedUser));
    }
}
