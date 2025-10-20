package com.koto.service.bo.appuser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserBo {

	private String email;

	private String username;

	private String password;
}