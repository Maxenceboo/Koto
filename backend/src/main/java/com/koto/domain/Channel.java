package com.koto.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "channel")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Channel {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false, length = 80)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@ManyToOne(optional = false)
	@JoinColumn(name = "created_by", nullable = false)
	private AppUser createdBy;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "is_invite_only", nullable = false)
	private boolean isInviteOnly = true;


}
