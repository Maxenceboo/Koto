package com.koto.web.controller;

import com.koto.service.AppUserService;
import com.koto.service.bo.appuser.AppUserBo;
import com.koto.web.dto.appuser.AppUserDto;
import com.koto.web.mapper.AppUserBoToDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

	public static final int EXPECTED = 200;

	@Mock
	private AppUserService service;

	@Mock
	private AppUserBoToDtoMapper mapper;

	@InjectMocks
	private AuthController controller;

	@Mock
	private Authentication authentication;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldReturnCurrentUserDto() {
		// Arrange
		final String email = "test@example.com";
		when(this.authentication.getName()).thenReturn(email);

		final AppUserBo bo = new AppUserBo();
		bo.setId(UUID.randomUUID());
		bo.setEmail(email);
		bo.setUsername("testuser");
		bo.setPassword("secret");
		bo.setCreatedAt(LocalDateTime.now());

		final AppUserDto dto = new AppUserDto();
		dto.setId(bo.getId());
		dto.setEmail(bo.getEmail());
		dto.setUsername(bo.getUsername());
		dto.setCreatedAt(bo.getCreatedAt());

		when(this.service.getByEmail(email)).thenReturn(bo);
		when(this.mapper.toDto(bo)).thenReturn(dto);

		// Act
		final ResponseEntity<AppUserDto> response = this.controller.getCurrentUser(this.authentication);

		// Assert
		assertEquals(EXPECTED, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(email, response.getBody().getEmail());
		assertEquals("testuser", response.getBody().getUsername());

		verify(this.service).getByEmail(email);
		verify(this.mapper).toDto(bo);
	}
}