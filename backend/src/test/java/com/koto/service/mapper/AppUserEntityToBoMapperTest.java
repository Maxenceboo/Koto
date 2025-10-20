package com.koto.service.mapper;

import com.koto.domain.AppUser;
import com.koto.service.bo.appuser.AppUserBo;
import com.koto.web.dto.appuser.AppUserDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppUserEntityToBoMapperTest {


	private final AppUserEntityToBoMapper mapper = new AppUserEntityToBoMapperImpl();

	@Test
	void shouldMapEntityToBo() {
		final AppUser entity = AppUser.builder()
				.id(UUID.randomUUID())
				.email("test@example.com")
				.username("testuser")
				.password("secret")
				.createdAt(LocalDateTime.now())
				.build();

		final AppUserBo bo = this.mapper.toBo(entity);

		assertNotNull(bo);
		assertEquals(entity.getId(), bo.getId());
		assertEquals(entity.getEmail(), bo.getEmail());
		assertEquals(entity.getUsername(), bo.getUsername());
		assertEquals(entity.getPassword(), bo.getPassword());
		assertEquals(entity.getCreatedAt(), bo.getCreatedAt());
	}

	@Test
	void shouldMapBoToDto() {
		final AppUserBo bo = new AppUserBo();
		bo.setId(UUID.randomUUID());
		bo.setEmail("test@example.com");
		bo.setUsername("testuser");
		bo.setPassword("secret");
		bo.setCreatedAt(LocalDateTime.now());

		final AppUserDto dto = this.mapper.toDto(bo);

		assertNotNull(dto);
		assertEquals(bo.getId(), dto.getId());
		assertEquals(bo.getEmail(), dto.getEmail());
		assertEquals(bo.getUsername(), dto.getUsername());
		assertEquals(bo.getCreatedAt(), dto.getCreatedAt());
	}
}
