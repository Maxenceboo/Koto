package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {
    private final AppUserRepository repo;

    public AppUser create(String email, String usernameGlobal) {
        if (repo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        AppUser user = AppUser.builder()
                .email(email)
                .usernameGlobal(usernameGlobal)
                .build();
        return repo.save(user);
    }

    public AppUser getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public AppUser update(UUID id, AppUser appUser) {
        if (repo.existsById(id)) {
            appUser.setId(id);
            return repo.save(appUser);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    public void delete(UUID id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    public List<AppUser> list() {
        if (repo.findAll().isEmpty()) {
            throw new EntityNotFoundException("Users not found");
        }
        return repo.findAll();
    }
}
