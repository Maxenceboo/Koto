package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.domain.Channel;
import com.koto.domain.ChannelMember;
import com.koto.repository.ChannelRepository;
import com.koto.service.bo.channel.ChannelBo;
import com.koto.service.bo.channel.ChannelSummaryBo;
import com.koto.service.bo.channel.CreateChannelBo;
import com.koto.service.mapper.ChannelEntityToBoMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChannelServiceTest {

	@Mock
	private ChannelRepository channelRepository;

	@Mock
	private ChannelEntityToBoMapper channelEntityToBoMapper;

	@Mock
	private EntityManager entityManager;

	@Mock
	private ChannelMemberService channelMemberService;

	@InjectMocks
	private ChannelService channelService;

	private UUID userId;
	private AppUser userRef;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.userId = UUID.randomUUID();
		this.userRef = new AppUser();
		this.userRef.setId(this.userId);
	}

	@Test
	void shouldCreateChannelSuccessfully() {
		// Arrange
		final CreateChannelBo createChannelBo = new CreateChannelBo();
		createChannelBo.setName("TestChannel");
		createChannelBo.setCreatedBy(this.userId);

		final Channel savedEntity = Channel.builder()
				.id(UUID.randomUUID())
				.name("TestChannel")
				.createdBy(this.userRef)
				.build();

		final ChannelBo channelBo = new ChannelBo();
		channelBo.setId(savedEntity.getId());
		channelBo.setName(savedEntity.getName());

		when(this.channelRepository.existsByname("TestChannel")).thenReturn(false);
		when(this.entityManager.getReference(AppUser.class, this.userId)).thenReturn(this.userRef);
		when(this.channelRepository.save(any(Channel.class))).thenReturn(savedEntity);
		when(this.channelEntityToBoMapper.toBo(savedEntity)).thenReturn(channelBo);

		// Act
		final ChannelBo result = this.channelService.create(createChannelBo);

		// Assert
		assertNotNull(result);
		assertEquals("TestChannel", result.getName());
		verify(this.channelRepository).save(any(Channel.class));
		verify(this.channelRepository).flush();
	}

	@Test
	void shouldThrowExceptionWhenNameExists() {
		// Arrange
		final CreateChannelBo createChannelBo = new CreateChannelBo();
		createChannelBo.setName("DuplicateChannel");
		createChannelBo.setCreatedBy(this.userId);

		when(this.channelRepository.existsByname("DuplicateChannel")).thenReturn(true);

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> this.channelService.create(createChannelBo));
		verify(this.channelRepository, never()).save(any());
	}

	@Test
	void shouldAddCreatorAsOwnerWhenChannelIsCreated() {
		// Arrange
		final CreateChannelBo createChannelBo = new CreateChannelBo();
		createChannelBo.setName("NewChannel");
		createChannelBo.setCreatedBy(this.userId);

		final Channel savedEntity = Channel.builder()
				.id(UUID.randomUUID())
				.name("NewChannel")
				.createdBy(this.userRef)
				.build();

		final ChannelBo channelBo = new ChannelBo();
		channelBo.setId(savedEntity.getId());
		channelBo.setName(savedEntity.getName());

		when(this.channelRepository.existsByname("NewChannel")).thenReturn(false);
		when(this.entityManager.getReference(AppUser.class, this.userId)).thenReturn(this.userRef);
		when(this.channelRepository.save(any(Channel.class))).thenReturn(savedEntity);
		when(this.channelEntityToBoMapper.toBo(savedEntity)).thenReturn(channelBo);

		// Act
		final ChannelBo result = this.channelService.create(createChannelBo);

		// Assert
		assertNotNull(result);
		verify(this.channelMemberService).addMemberInToChannel(
				argThat(bo -> bo.getChannelId().equals(savedEntity.getId())
						&& bo.getUserId().equals(this.userId)
						&& bo.getRole().equals("owner"))
		);
	}

	@Test
	void shouldReturnChannelSummariesForUser() {
		// Arrange
		final Channel channel1 = Channel.builder().id(UUID.randomUUID()).name("Channel1").build();
		final Channel channel2 = Channel.builder().id(UUID.randomUUID()).name("Channel2").build();

		final ChannelMember membership1 = new ChannelMember();
		membership1.setChannel(channel1);

		final ChannelMember membership2 = new ChannelMember();
		membership2.setChannel(channel2);

		this.userRef.setMemberships(List.of(membership1, membership2));

		when(this.entityManager.getReference(AppUser.class, this.userId)).thenReturn(this.userRef);

		// Act
		final List<ChannelSummaryBo> result = this.channelService.getChannelsForUser(this.userId);

		// Assert
		assertEquals(2, result.size());
		assertTrue(result.stream().anyMatch(cs -> cs.getName().equals("Channel1")));
		assertTrue(result.stream().anyMatch(cs -> cs.getName().equals("Channel2")));
	}
}