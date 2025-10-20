package com.koto.service.mapper;

import com.koto.domain.Channel;
import com.koto.service.bo.channel.ChannelBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChannelEntityToBoMapper {
	@Mapping(source = "createdBy.id", target = "createdBy")
	ChannelBo toBo (final Channel entity);

}
