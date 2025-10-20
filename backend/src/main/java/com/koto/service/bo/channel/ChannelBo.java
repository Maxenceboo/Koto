package com.koto.service.bo.channel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ChannelBo {

	private UUID id;

	private String name;

	private String description;

	private UUID createdBy;

	private LocalDateTime createdAt;

}