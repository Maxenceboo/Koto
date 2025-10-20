package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppUserDetailsServiceTest {

	private final AppUserRepository repository = mock(AppUserRepository.class);
	private final AppUserDetailsService service = new AppUserDetailsService(this.repository);

	@Test
	void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
		// Arrange
		final AppUser user = new AppUser();
		user.setEmail("test@example.com");
		user.setPassword("securePassword");

		when(this.repository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		// Act
		final UserDetails userDetails = this.service.loadUserByUsername("test@example.com");

		// Assert
		assertEquals("test@example.com", userDetails.getUsername());
		assertEquals("securePassword", userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
	}

	@Test
	void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
		// Arrange
		when(this.repository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(UsernameNotFoundException.class, () -> {
			this.service.loadUserByUsername("unknown@example.com");
		});
	}
}