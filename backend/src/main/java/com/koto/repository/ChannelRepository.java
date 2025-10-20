package com.koto.repository;


import com.koto.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
	boolean existsByname(String name);
}
