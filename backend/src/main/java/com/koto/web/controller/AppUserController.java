package com.koto.web.controller;

import com.koto.domain.AppUser;
import com.koto.service.AppUserService;
import com.koto.service.bo.appuser.AppUserBo;
import com.koto.web.dto.appuser.AppUserDto;
import com.koto.web.mapper.AppUserBoToDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "AppUser", description = "Operations related to application users")
public class AppUserController {

	@Autowired
	private final AppUserService service;

	@Autowired
	private final AppUserBoToDtoMapper mapper;

	@Operation(summary = "Lister tous les utilisateurs")
	@GetMapping
	public List<AppUserDto> getAllUsers() {
		return service.list().stream()
				.map(AppUserMapper::toDto)
				.toList();
	}

	@Operation(summary = "Récupérer un utilisateur par ID")
	@GetMapping("/{id}")
	public ResponseEntity<AppUserDto> getUserById(@PathVariable UUID id) {
		AppUser user = service.getById(id);
		return ResponseEntity.ok(AppUserMapper.toDto(user));
	}

	@Operation(summary = "créer un utilisateur")
	@PostMapping
	public ResponseEntity<AppUserDto> createUser(@Valid @RequestBody AppUserBo user) {
		AppUserBo createdUser = service.create(user.getEmail(), user.getUsernameGlobal());
		return ResponseEntity.created(URI.create("/api/user/" + createdUser.getId()))
				.body(mapper.toDto(createdUser));
	}

	@Operation(summary = "Mettre à jour un utilisateur par ID")
	@PutMapping("/{id}")
	public ResponseEntity<AppUserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody AppUser user) {
		// dto to bo
		AppUser updatedUser = service.update(id, user);
		// bo to dto
		return ResponseEntity.ok(AppUserMapper.toDto(updatedUser));
	}
}
