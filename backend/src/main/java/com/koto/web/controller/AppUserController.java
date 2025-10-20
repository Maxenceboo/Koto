package com.koto.web.controller;

import com.koto.service.AppUserService;
import com.koto.service.bo.appuser.AppUserBo;
import com.koto.web.dto.appuser.AppUserDto;
import com.koto.web.dto.appuser.CreateUserDto;
import com.koto.web.mapper.AppUserBoToDtoMapper;
import com.koto.web.mapper.CreateUserBoToDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "AppUser", description = "Operations related to application users")
public class AppUserController {

	private final AppUserService service;

	private final AppUserBoToDtoMapper mapper;

	private final CreateUserBoToDtoMapper createMapper;

	private final PasswordEncoder passwordEncoder;

	@Operation(summary = "créer un utilisateur")
	@PostMapping
	public ResponseEntity<AppUserDto> createUser(@RequestBody final CreateUserDto dto) {
		dto.setPassword(this.passwordEncoder.encode(dto.getPassword()));
		final AppUserBo createdUser = this.service.create(this.createMapper.toBo(dto));
		return ResponseEntity.created(URI.create("/api/user/" + createdUser.getId()))
				.body(this.mapper.toDto(createdUser));
	}
}
