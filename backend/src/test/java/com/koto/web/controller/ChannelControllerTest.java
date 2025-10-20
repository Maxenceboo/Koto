package com.koto.web.controller;

import com.koto.service.ChannelService;
import com.koto.service.bo.channel.ChannelBo;
import com.koto.service.bo.channel.ChannelSummaryBo;
import com.koto.service.bo.channel.CreateChannelBo;
import com.koto.web.dto.channel.ChannelDto;
import com.koto.web.dto.channel.ChannelSummaryDto;
import com.koto.web.dto.channel.CreateChannelDto;
import com.koto.web.mapper.ChannelBoToDtoMapper;
import com.koto.web.mapper.ChannelSummarBoToDtoMapper;
import com.koto.web.mapper.CreateChannelBoToDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChannelControllerTest {

	@Mock
	private ChannelService channelService;

	@Mock
	private CreateChannelBoToDtoMapper createMapper;

	@Mock
	private ChannelBoToDtoMapper channelMapper;

	@Mock
	private ChannelSummarBoToDtoMapper channelSummaryMapper;

	@InjectMocks
	private ChannelController channelController;

	private UUID userId;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.userId = UUID.randomUUID();
	}

	@Test
	void createChannel_shouldReturnChannelDto() {
		// Arrange
		final CreateChannelDto dto = new CreateChannelDto();
		dto.setName("Canal Test");
		dto.setCreatedBy(this.userId);

		final CreateChannelBo bo = new CreateChannelBo();
		bo.setName("Canal Test");
		bo.setCreatedBy(this.userId);

		final ChannelBo channelBo = new ChannelBo();
		channelBo.setId(UUID.randomUUID());
		channelBo.setName("Canal Test");
		channelBo.setDescription("Description");
		channelBo.setCreatedBy(this.userId);

		final ChannelDto channelDto = new ChannelDto();
		channelDto.setId(channelBo.getId());
		channelDto.setName("Canal Test");
		channelDto.setDescription("Description");
		channelDto.setCreatedBy(this.userId);

		when(this.createMapper.toBo(dto)).thenReturn(bo);
		when(this.channelService.create(bo)).thenReturn(channelBo);
		when(this.channelMapper.toDto(channelBo)).thenReturn(channelDto);

		// Act
		final ResponseEntity<ChannelDto> response = this.channelController.createChannel(dto);

		// Assert
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isEqualTo(channelDto);

		verify(this.createMapper).toBo(dto);
		verify(this.channelService).create(bo);
		verify(this.channelMapper).toDto(channelBo);
	}

	@Test
	void getChannel_shouldReturnListOfChannelSummaryDtos() {
		// Arrange
		final ChannelSummaryBo bo1 = new ChannelSummaryBo(UUID.randomUUID(), "Canal 1");
		final ChannelSummaryBo bo2 = new ChannelSummaryBo(UUID.randomUUID(), "Canal 2");

		final ChannelSummaryDto dto1 = new ChannelSummaryDto();
		dto1.setId(bo1.getId());
		dto1.setName(bo1.getName());

		final ChannelSummaryDto dto2 = new ChannelSummaryDto();
		dto2.setId(bo2.getId());
		dto2.setName(bo2.getName());

		when(this.channelService.getChannelsForUser(this.userId)).thenReturn(List.of(bo1, bo2));
		when(this.channelSummaryMapper.toDtos(List.of(bo1, bo2))).thenReturn(List.of(dto1, dto2));

		// Act
		final ResponseEntity<List<ChannelSummaryDto>> response = this.channelController.getChannel(this.userId);

		// Assert
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).containsExactly(dto1, dto2);

		verify(this.channelService).getChannelsForUser(this.userId);
		verify(this.channelSummaryMapper).toDtos(List.of(bo1, bo2));
	}
}