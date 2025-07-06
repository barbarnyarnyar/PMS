package com.tolimoli.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long>,
    JpaSpecificationExecutor<Channel> {

}
