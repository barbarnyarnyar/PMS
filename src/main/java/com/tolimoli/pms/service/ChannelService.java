package com.tolimoli.pms.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tolimoli.pms.entity.Channel;
import com.tolimoli.pms.entity.Rate;
import com.tolimoli.pms.entity.Room;
import java.util.Optional;
import com.tolimoli.pms.repository.ChannelRepository;
import com.tolimoli.pms.repository.RateRepository;

import jakarta.transaction.Transactional;

// ===== 3. CHANNEL SERVICE =====
@Service
@Transactional
public class ChannelService {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private RateRepository rateRepository;

  // Create channel
  public Channel createChannel(String channelName, String channelCode,
      BigDecimal commissionRate, String apiEndpoint) {
    Channel channel = new Channel();
    channel.setChannelName(channelName);
    channel.setChannelCode(channelCode);
    channel.setCommissionRate(commissionRate);
    channel.setApiEndpoint(apiEndpoint);
    channel.setIsActive(true);

    return channelRepository.save(channel);
  }

  // Get all active channels
  public List<Channel> getAllActiveChannels() {
    return channelRepository.findByIsActiveTrue();
  }

  // View available rates in channel
  public List<Rate> viewChannelRates(Long channelId, LocalDate date) {
    return rateRepository.findByChannelIdAndRateDate(channelId, date);
  }

  // Sync rates to channel
  public void syncRatesToChannel(Long roomId, Long channelId, LocalDate date,
      BigDecimal rate, Integer availability) {
    Rate rateRecord = new Rate();
    rateRecord.setRoom(new Room()); // Set room
    rateRecord.setChannel(new Channel()); // Set channel
    rateRecord.setRateDate(date);
    rateRecord.setRateAmount(rate);
    rateRecord.setAvailableRooms(availability);

    rateRepository.save(rateRecord);
  }

  // Get channel by code
  public Optional<Channel> getChannelByCode(String channelCode) {
    return channelRepository.findByChannelCode(channelCode);
  }
}
