package com.koto.web.mapper;

import com.koto.service.bo.channel.CreateChannelBo;
import com.koto.web.dto.channel.CreateChannelDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateChannelBoToDtoMapper {
	CreateChannelDto toDto (CreateChannelBo bo);
	CreateChannelBo toBo (CreateChannelDto dto);
}
