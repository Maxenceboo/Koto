package com.koto.service.mapper;

import com.koto.domain.ChannelMember;
import com.koto.service.bo.ChannelMember.ChannelMemberBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChannelMemberEntityToBoMapper {
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "channelId", source = "channel.id")
	ChannelMemberBo toBo (final ChannelMember entity);

}
