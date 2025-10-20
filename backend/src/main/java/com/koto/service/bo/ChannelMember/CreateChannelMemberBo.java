package com.koto.service.bo.ChannelMember;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChannelMemberBo {
	private UUID channelId;
	private UUID userId;
	private String role;
}