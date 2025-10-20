package com.koto.repository;

import com.koto.domain.AppUser;
import com.koto.domain.Channel;
import com.koto.domain.ChannelMember;
import com.koto.domain.ChannelMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, ChannelMemberId> {
	boolean existsByChannelAndUser(Channel channel, AppUser user);
}
