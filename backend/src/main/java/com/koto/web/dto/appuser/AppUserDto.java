package com.koto.web.dto.appuser;

import java.time.Instant;
import java.util.UUID;

public record AppUserDto(UUID id, String username, Instant createdAt) {}
