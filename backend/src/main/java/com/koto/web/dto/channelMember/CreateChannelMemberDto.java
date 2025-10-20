package com.koto.web.dto.channelMember;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateChannelMemberDto {
	private UUID channelId;
	private UUID userId;
	private String role;
}