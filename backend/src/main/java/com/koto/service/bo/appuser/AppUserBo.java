package com.koto.service.bo.appuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserBo {

	private UUID id;

	private String email;

	private String usernameGlobal;

	private Instant createdAt;

	private String password;
}