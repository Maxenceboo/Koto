package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.repository.AppUserRepository;

public class AuthService {
    private final AppUserRepository userRepository;


    public AppUser register(AppUser appUser) {
        if (userRepository.findByEmail(appUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(appUser);
    }

}
