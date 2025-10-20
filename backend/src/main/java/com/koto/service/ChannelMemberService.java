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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberService {
	private final ChannelMemberRepository channelMemberRepository;
	private final EntityManager em;

	private final ChannelMemberEntityToBoMapper channelMemberEntityToBoMapper;

	public ChannelMemberBo addMemberInToChannel (final CreateChannelMemberBo bo) {
		final Channel channelRef = this.em.getReference(Channel.class, bo.getChannelId());
		final AppUser userRef= this.em.getReference(AppUser.class, bo.getUserId());

		if (this.channelMemberRepository.existsByChannelAndUser(channelRef, userRef)) {
			throw new IllegalArgumentException("User already in channel");
		}


		final ChannelMember member = ChannelMember.builder()
				.id(new ChannelMemberId(bo.getChannelId(), bo.getUserId()))
				.channel(channelRef)
				.user(userRef)
				.role(bo.getRole() != null ? bo.getRole() : "member")
				.build();

		return this.channelMemberEntityToBoMapper.toBo(this.channelMemberRepository.save(member));
	}
}
