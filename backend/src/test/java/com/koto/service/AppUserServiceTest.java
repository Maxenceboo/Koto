package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.repository.AppUserRepository;
import com.koto.service.bo.appuser.AppUserBo;
import com.koto.service.bo.appuser.CreateUserBo;
import com.koto.service.mapper.AppUserEntityToBoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

	@Mock
	private AppUserRepository repo;

	@Mock
	private AppUserEntityToBoMapper mapper;

	@InjectMocks
	private AppUserService service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldCreateUserSuccessfully() {
		String email = "test@example.com";
		String username = "testuser";
		String password = "secret";

		AppUser entity = AppUser.builder()
				.id(UUID.randomUUID())
				.email(email)
				.username(username)
				.password(password)
				.createdAt(LocalDateTime.now())
				.build();

		AppUserBo bo = new AppUserBo();
		bo.setId(entity.getId());
		bo.setEmail(email);
		bo.setUsername(username);
		bo.setPassword(password);
		bo.setCreatedAt(entity.getCreatedAt());

		when(repo.existsByEmail(email)).thenReturn(false);
		when(repo.save(any(AppUser.class))).thenReturn(entity);
		when(mapper.toBo(entity)).thenReturn(bo);

		CreateUserBo bo2 = new CreateUserBo();
		bo2.setEmail(email);
		bo2.setUsername(username);
		bo2.setPassword(password);

		AppUserBo result = service.create(bo2);

		assertNotNull(result);
		assertEquals(email, result.getEmail());
		assertEquals(username, result.getUsername());
		assertEquals(password, result.getPassword());
		assertNotNull(result.getId());
		assertNotNull(result.getCreatedAt());

		verify(repo).save(any(AppUser.class));
		verify(mapper).toBo(entity);
	}

	@Test
	void shouldThrowExceptionWhenEmailExists() {

		String email = "existing@example.com";
		String username = "testuser";
		String password = "secret";

		CreateUserBo bo2 = new CreateUserBo();
		bo2.setEmail(email);
		bo2.setUsername(username);
		bo2.setPassword(password);

		when(repo.existsByEmail(email)).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () ->
				service.create(bo2));
	}

	@Test
	void shouldReturnUserBoWhenEmailExists() {
		String email = "test@example.com";

		AppUser entity = AppUser.builder()
				.id(UUID.randomUUID())
				.email(email)
				.username("testuser")
				.password("secret")
				.createdAt(LocalDateTime.now())
				.build();

		AppUserBo bo = new AppUserBo();
		bo.setId(entity.getId());
		bo.setEmail(entity.getEmail());
		bo.setUsername(entity.getUsername());
		bo.setPassword(entity.getPassword());
		bo.setCreatedAt(entity.getCreatedAt());

		when(repo.findByEmail(email)).thenReturn(Optional.of(entity));
		when(mapper.toBo(entity)).thenReturn(bo);

		AppUserBo result = service.getByEmail(email);

		assertNotNull(result);
		assertEquals(email, result.getEmail());
		assertEquals("testuser", result.getUsername());
		verify(repo).findByEmail(email);
		verify(mapper).toBo(entity);
	}

	@Test
	void shouldThrowExceptionWhenEmailNotFound() {
		String email = "unknown@example.com";

		when(repo.findByEmail(email)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> service.getByEmail(email));
		verify(repo).findByEmail(email);
	}

}