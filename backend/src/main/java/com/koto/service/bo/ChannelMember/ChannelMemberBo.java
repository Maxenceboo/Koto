package com.koto.service.bo.ChannelMember;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ChannelMemberBo {
	private UUID channelId;
	private UUID userId;
	private String role;
	private LocalDateTime joinedAt;
}

