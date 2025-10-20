package com.koto.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "channel_member")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChannelMember {

	@EmbeddedId
	private ChannelMemberId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("channelId")
	@JoinColumn(name = "channel_id", nullable = false)
	private Channel channel;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser user;

	@Column(nullable = false, length = 20)
	private String role = "member"; // owner | member

	@CreationTimestamp
	@Column(name = "joined_at", nullable = false, updatable = false)
	private LocalDateTime joinedAt;
}