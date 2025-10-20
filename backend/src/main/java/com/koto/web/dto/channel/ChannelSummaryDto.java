package com.koto.web.dto.channel;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChannelSummaryDto {
	private UUID id;
	private String name;
}
