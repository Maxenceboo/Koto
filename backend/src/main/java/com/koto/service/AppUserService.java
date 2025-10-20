package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.repository.AppUserRepository;
import com.koto.service.bo.appuser.AppUserBo;
import com.koto.service.bo.appuser.CreateUserBo;
import com.koto.service.mapper.AppUserEntityToBoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {

	private final AppUserRepository repo;

	private final AppUserEntityToBoMapper mapper;

	@Transactional
	public AppUserBo create(final CreateUserBo createUserBo) {

		final String email = createUserBo.getEmail();
		final String username = createUserBo.getUsername();
		final String password = createUserBo.getPassword();

		if (this.repo.existsByEmail(email)) {
			throw new IllegalArgumentException("Email already in use");
		}

		final AppUser user = AppUser.builder().email(email).username(username).password(password).build();

		final AppUser saved = this.repo.save(user);

		this.repo.flush();

		return this.mapper.toBo(saved);
	}

	@Transactional
	public AppUserBo getByEmail(final String email) {
		final AppUser user = this.repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return this.mapper.toBo(user);
	}

}
