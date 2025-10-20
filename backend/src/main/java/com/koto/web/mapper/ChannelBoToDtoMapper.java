package com.koto.web.mapper;

import com.koto.service.bo.channel.ChannelBo;
import com.koto.web.dto.channel.ChannelDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelBoToDtoMapper {
	ChannelBo toBo(ChannelDto dto);
	ChannelDto toDto(ChannelBo bo);
}
