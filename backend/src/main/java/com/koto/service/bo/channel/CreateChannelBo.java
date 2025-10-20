package com.koto.service.bo.channel;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateChannelBo {

	private String name;

	private UUID createdBy;
}
