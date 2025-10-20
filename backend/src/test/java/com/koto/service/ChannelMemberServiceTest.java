package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.domain.Channel;
import com.koto.domain.ChannelMember;
import com.koto.domain.ChannelMemberId;
import com.koto.repository.ChannelMemberRepository;
import com.koto.service.bo.ChannelMember.ChannelMemberBo;
import com.koto.service.bo.ChannelMember.CreateChannelMemberBo;
import com.koto.service.mapper.ChannelMemberEntityToBoMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChannelMemberServiceTest {

	@Mock
	private ChannelMemberRepository channelMemberRepository;

	@Mock
	private EntityManager entityManager;

	@Mock
	private ChannelMemberEntityToBoMapper channelMemberEntityToBoMapper;

	@InjectMocks
	private ChannelMemberService channelMemberService;

	private UUID channelId;
	private UUID userId;
	private Channel channelRef;
	private AppUser userRef;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.channelId = UUID.randomUUID();
		this.userId = UUID.randomUUID();

		this.channelRef = Channel.builder().id(this.channelId).name("TestChannel").build();
		this.userRef = new AppUser();
		this.userRef.setId(this.userId);
	}

	@Test
	void shouldAddMemberToChannelSuccessfully() {
		// Arrange
		final CreateChannelMemberBo bo = new CreateChannelMemberBo(this.channelId, this.userId, "member");

		final ChannelMember member = ChannelMember.builder()
				.id(new ChannelMemberId(this.channelId, this.userId))
				.channel(this.channelRef)
				.user(this.userRef)
				.role("member")
				.build();

		final ChannelMemberBo expectedBo = new ChannelMemberBo();
		expectedBo.setChannelId(this.channelId);
		expectedBo.setUserId(this.userId);
		expectedBo.setRole("member");

		when(this.entityManager.getReference(Channel.class, this.channelId)).thenReturn(this.channelRef);
		when(this.entityManager.getReference(AppUser.class, this.userId)).thenReturn(this.userRef);
		when(this.channelMemberRepository.existsByChannelAndUser(this.channelRef, this.userRef)).thenReturn(false);
		when(this.channelMemberRepository.save(any(ChannelMember.class))).thenReturn(member);
		when(this.channelMemberEntityToBoMapper.toBo(member)).thenReturn(expectedBo);

		// Act
		final ChannelMemberBo result = this.channelMemberService.addMemberInToChannel(bo);

		// Assert
		assertNotNull(result);
		assertEquals(this.channelId, result.getChannelId());
		assertEquals(this.userId, result.getUserId());
		assertEquals("member", result.getRole());
		verify(this.channelMemberRepository).save(any(ChannelMember.class));
	}

	@Test
	void shouldThrowExceptionIfUserAlreadyInChannel() {
		// Arrange
		final CreateChannelMemberBo bo = new CreateChannelMemberBo(this.channelId, this.userId, "member");

		when(this.entityManager.getReference(Channel.class, this.channelId)).thenReturn(this.channelRef);
		when(this.entityManager.getReference(AppUser.class, this.userId)).thenReturn(this.userRef);
		when(this.channelMemberRepository.existsByChannelAndUser(this.channelRef, this.userRef)).thenReturn(true);

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> this.channelMemberService.addMemberInToChannel(bo));
		verify(this.channelMemberRepository, never()).save(any());
	}
}