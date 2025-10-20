package com.koto.web.mapper;

import com.koto.service.bo.channel.ChannelSummaryBo;
import com.koto.web.dto.channel.ChannelSummaryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChannelSummarBoToDtoMapper {
	List<ChannelSummaryDto> toDtos (List<ChannelSummaryBo> bos);
}
