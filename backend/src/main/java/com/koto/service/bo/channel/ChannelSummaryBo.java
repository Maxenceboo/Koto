package com.koto.service.bo.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChannelSummaryBo {
	private UUID id;
	private String name;
}
