package com.koto.service.bo.appuser;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppUserBo {

	private UUID id;

	private String email;

	private String username;

	private LocalDateTime createdAt;

	private String password;
}