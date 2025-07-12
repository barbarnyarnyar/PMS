package com.tolimoli.pms.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tolimoli.pms.entity.Channel;
import com.tolimoli.pms.entity.Rate;
import com.tolimoli.pms.entity.Room;
import com.tolimoli.pms.exception.ResourceNotFoundException;
import com.tolimoli.pms.exception.BusinessLogicException;
import java.util.Optional;
import com.tolimoli.pms.repository.ChannelRepository;
import com.tolimoli.pms.repository.RateRepository;
import com.tolimoli.pms.repository.RoomRepository;

import javax.transaction.Transactional;

// ===== 3. CHANNEL SERVICE =====
@Service
@Transactional
public class ChannelService {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private RateRepository rateRepository;

  @Autowired
  private RoomRepository roomRepository;

  // Create channel
  public Channel createChannel(String channelName, String channelCode,
      BigDecimal commissionRate, String apiEndpoint) {
    
    // Validate input parameters
    if (channelName == null || channelName.trim().isEmpty()) {
      throw new IllegalArgumentException("Channel name is required");
    }
    if (channelCode == null || channelCode.trim().isEmpty()) {
      throw new IllegalArgumentException("Channel code is required");
    }
    
    // Check for duplicate channel code
    if (channelRepository.existsByChannelCode(channelCode)) {
      throw new BusinessLogicException("Channel with code '" + channelCode + "' already exists");
    }
    
    // Check for duplicate channel name
    if (channelRepository.existsByChannelName(channelName)) {
      throw new BusinessLogicException("Channel with name '" + channelName + "' already exists");
    }
    
    // Validate commission rate
    if (commissionRate != null && (commissionRate.compareTo(BigDecimal.ZERO) < 0 || 
        commissionRate.compareTo(new BigDecimal("100")) > 0)) {
      throw new IllegalArgumentException("Commission rate must be between 0 and 100");
    }
    
    Channel channel = new Channel();
    channel.setChannelName(channelName.trim());
    channel.setChannelCode(channelCode.trim().toUpperCase());
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
    
    // Validate input parameters
    if (roomId == null || channelId == null || date == null || rate == null || availability == null) {
      throw new IllegalArgumentException("All parameters are required for rate synchronization");
    }
    
    // Get actual room and channel entities from database
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new ResourceNotFoundException("Room", roomId));
    
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel", channelId));
    
    // Check if rate already exists for this combination
    Optional<Rate> existingRate = rateRepository.findByRoomIdAndChannelIdAndRateDate(
        roomId, channelId, date);
    
    Rate rateRecord;
    if (existingRate.isPresent()) {
      // Update existing rate
      rateRecord = existingRate.get();
      rateRecord.updateRate(rate);
      rateRecord.updateAvailability(availability);
    } else {
      // Create new rate
      rateRecord = new Rate();
      rateRecord.setRoom(room);
      rateRecord.setChannel(channel);
      rateRecord.setRateDate(date);
      rateRecord.setRateAmount(rate);
      rateRecord.setAvailableRooms(availability);
    }

    rateRepository.save(rateRecord);
  }

  // Get channel by code
  public Optional<Channel> getChannelByCode(String channelCode) {
    return channelRepository.findByChannelCode(channelCode);
  }
}
