package com.koto.web.controller;

import com.koto.service.AppUserService;
import com.koto.service.bo.appuser.AppUserBo;
import com.koto.service.bo.appuser.CreateUserBo;
import com.koto.web.dto.appuser.AppUserDto;
import com.koto.web.dto.appuser.CreateUserDto;
import com.koto.web.mapper.AppUserBoToDtoMapper;
import com.koto.web.mapper.CreateUserBoToDtoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AppUserControllerTest {

	public static final int EXPECTED = 201;

	public static final UUID ID = UUID.randomUUID();

	@Mock
	private AppUserService service;

	@Mock
	private AppUserBoToDtoMapper mapper;

	@Mock
	private CreateUserBoToDtoMapper createMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AppUserController controller;

	public AppUserControllerTest() {
		openMocks(this);
	}

	@Test
	void createUser_shouldReturnCreatedUserDto() {
		// Arrange
		final CreateUserDto dto = new CreateUserDto();
		dto.setPassword("plainPassword");

		final CreateUserDto hashedDto = new CreateUserDto();
		hashedDto.setPassword("hashedPassword");

		final AppUserBo bo = new AppUserBo();
		bo.setId(ID);

		final CreateUserBo createBo = new CreateUserBo();


		final AppUserDto expectedDto = new AppUserDto();
		expectedDto.setId(ID);

		when(this.passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
		when(this.createMapper.toBo(dto)).thenReturn(createBo);
		when(this.service.create(createBo)).thenReturn(bo);
		when(this.mapper.toDto(bo)).thenReturn(expectedDto);

		// Act
		final ResponseEntity<AppUserDto> response = this.controller.createUser(dto);

		// Assert
		assertThat(response.getStatusCode().value()).isEqualTo(EXPECTED);
		assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/api/user/"+ID));
		assertThat(response.getBody()).isEqualTo(expectedDto);

		verify(this.passwordEncoder).encode("plainPassword");
		verify(this.createMapper).toBo(dto);
		verify(this.service).create(createBo);
		verify(this.mapper).toDto(bo);
	}
}