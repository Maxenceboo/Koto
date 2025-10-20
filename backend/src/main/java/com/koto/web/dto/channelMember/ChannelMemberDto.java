package com.koto.web.dto.channelMember;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ChannelMemberDto {
	private UUID channelId;
	private UUID userId;
	private String role;
	private LocalDateTime joinedAt;
}
