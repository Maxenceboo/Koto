package com.koto.web.dto.appuser;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppUserDto {

	private UUID id;

	private String email;

	private String username;

	private LocalDateTime createdAt;

}
