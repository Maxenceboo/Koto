package com.koto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMemberId {

	@Column(name = "channel_id")
	private UUID channelId;

	@Column(name = "user_id")
	private UUID userId;

}