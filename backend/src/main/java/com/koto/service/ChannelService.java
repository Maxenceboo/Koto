package com.koto.service;

import com.koto.domain.AppUser;
import com.koto.domain.Channel;
import com.koto.repository.ChannelRepository;
import com.koto.service.bo.ChannelMember.CreateChannelMemberBo;
import com.koto.service.bo.channel.ChannelBo;
import com.koto.service.bo.channel.ChannelSummaryBo;
import com.koto.service.bo.channel.CreateChannelBo;
import com.koto.service.mapper.ChannelEntityToBoMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelService {

	private final ChannelRepository  channelRepository;

	private final ChannelEntityToBoMapper channelEntityToBoMapper;

	private final EntityManager entityManager;

	private final ChannelMemberService channelMemberService;

	@Transactional
	public ChannelBo create(final CreateChannelBo createChannelBo) {

		if (this.channelRepository.existsByname(createChannelBo.getName())) {
			throw new IllegalArgumentException("Name already exist");
		}

		final AppUser appUserRef = this.entityManager.getReference(AppUser.class, createChannelBo.getCreatedBy());

		final Channel channel = Channel.builder()
				.name(createChannelBo.getName())
				.createdBy(appUserRef)
				.build();

		final Channel saved = this.channelRepository.save(channel);

		this.channelMemberService.addMemberInToChannel(new CreateChannelMemberBo(saved.getId(), saved.getCreatedBy().getId(), "owner"));

		this.channelRepository.flush();

		return this.channelEntityToBoMapper.toBo(saved);
	}

	@Transactional
	public List<ChannelSummaryBo> getChannelsForUser(final UUID userId) {
		final AppUser user =  this.entityManager.getReference(AppUser.class, userId);

		return user.getMemberships().stream()
				.map(cm -> new ChannelSummaryBo(cm.getChannel().getId(), cm.getChannel().getName()))
				.toList();
	}
}
