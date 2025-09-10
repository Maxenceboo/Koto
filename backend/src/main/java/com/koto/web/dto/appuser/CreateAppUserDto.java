package com.koto.web.dto.appuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAppUserDto(@NotBlank @Size(min = 3, max = 50) String username) {} // Add validation annotations as needed
