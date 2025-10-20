package com.koto.web.mapper;

import com.koto.service.bo.appuser.CreateUserBo;
import com.koto.web.dto.appuser.CreateUserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateUserBoToDtoMapperTest {

	private final CreateUserBoToDtoMapper mapper = new CreateUserBoToDtoMapperImpl();

	@Test
	void shouldMapBoToDtoCorrectly() {
		final CreateUserBo bo = new CreateUserBo();
		bo.setEmail("test@example.com");
		bo.setUsername("testuser");
		bo.setPassword("secret");

		final CreateUserDto dto = this.mapper.toDto(bo);

		assertNotNull(dto);
		assertEquals("test@example.com", dto.getEmail());
		assertEquals("testuser", dto.getUsername());
		assertEquals("secret", dto.getPassword());
	}

	@Test
	void shouldMapDtoToBoCorrectly() {
		final CreateUserDto dto = new CreateUserDto();
		dto.setEmail("test@example.com");
		dto.setUsername("testuser");
		dto.setPassword("secret");

		final CreateUserBo bo = this.mapper.toBo(dto);

		assertNotNull(bo);
		assertEquals("test@example.com", bo.getEmail());
		assertEquals("testuser", bo.getUsername());
		assertEquals("secret", bo.getPassword());
	}
}