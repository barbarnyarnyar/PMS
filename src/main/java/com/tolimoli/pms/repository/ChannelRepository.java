package com.tolimoli.pms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long>,
    JpaSpecificationExecutor<Channel> {

    List<Channel> findByIsActiveTrue();

    List<Channel> findByChannelNameContainingIgnoreCase(String channelName);

    List<Channel> findByChannelCodeContainingIgnoreCase(String channelCode);

    List<Channel> findByApiEndpointContainingIgnoreCase(String apiEndpoint);

    List<Channel> findByCommissionRateBetween(Double minRate, Double maxRate);

    List<Channel> findByIsActive(Boolean isActive);

    Optional<Channel> findByChannelCode(String channelCode);
}
