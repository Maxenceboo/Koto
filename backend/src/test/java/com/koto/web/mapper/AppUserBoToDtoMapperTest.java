package com.koto.web.mapper;

import com.koto.service.bo.appuser.AppUserBo;
import com.koto.web.dto.appuser.AppUserDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AppUserBoToDtoMapperTest {

	private final AppUserBoToDtoMapper mapper = new AppUserBoToDtoMapperImpl();

	@Test
	void shouldMapBoToDtoCorrectly() {
		final UUID id = UUID.randomUUID();
		final LocalDateTime createdAt = LocalDateTime.now();

		final AppUserBo bo = new AppUserBo();
		bo.setId(id);
		bo.setEmail("test@example.com");
		bo.setUsername("testuser");
		bo.setPassword("secret");
		bo.setCreatedAt(createdAt);

		final AppUserDto dto = this.mapper.toDto(bo);

		assertNotNull(dto);
		assertEquals("test@example.com", dto.getEmail());
		assertEquals("testuser", dto.getUsername());
		assertEquals(id, dto.getId());
		assertEquals(createdAt, dto.getCreatedAt());
	}

	@Test
	void shouldReturnNullWhenBoIsNull() {
		final AppUserDto dto = this.mapper.toDto(null);
		assertNull(dto);
	}
}