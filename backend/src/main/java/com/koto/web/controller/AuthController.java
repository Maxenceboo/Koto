package com.koto.web.controller;

import com.koto.service.AppUserService;
import com.koto.web.dto.appuser.AppUserDto;
import com.koto.web.mapper.AppUserBoToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AppUserService service;

	private final AppUserBoToDtoMapper mapper;

	@GetMapping("/me")
	public ResponseEntity<AppUserDto> getCurrentUser(final Authentication authentication) {

		final AppUserDto response = this.mapper.toDto(this.service.getByEmail(authentication.getName()));

		return ResponseEntity.ok(response);
	}
}
