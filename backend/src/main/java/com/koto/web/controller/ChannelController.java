package com.koto.web.controller;

import com.koto.service.ChannelService;
import com.koto.service.bo.channel.CreateChannelBo;
import com.koto.web.dto.channel.ChannelDto;
import com.koto.web.dto.channel.ChannelSummaryDto;
import com.koto.web.dto.channel.CreateChannelDto;
import com.koto.web.mapper.ChannelBoToDtoMapper;
import com.koto.web.mapper.ChannelSummarBoToDtoMapper;
import com.koto.web.mapper.CreateChannelBoToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
	private final ChannelService service;

	private final CreateChannelBoToDtoMapper createChannelBoToDtoMapper;
	private final ChannelBoToDtoMapper channelBoToDtoMapper;
	private final ChannelSummarBoToDtoMapper channelSummarBoToDtoMapper;

	@PostMapping
	public ResponseEntity<ChannelDto> createChannel(@RequestBody final CreateChannelDto dto) {
		final CreateChannelBo createChannelBo = this.createChannelBoToDtoMapper.toBo(dto);
		final ChannelDto response = this.channelBoToDtoMapper.toDto(this.service.create(createChannelBo));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<ChannelSummaryDto>> getChannel(@PathVariable final UUID userId) {
		return ResponseEntity.ok(this.channelSummarBoToDtoMapper.toDtos(this.service.getChannelsForUser(userId)));
	}

}
