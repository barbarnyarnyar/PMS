package com.tolimoli.pms.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tolimoli.pms.entity.Rate;
import com.tolimoli.pms.entity.Room;
import com.tolimoli.pms.entity.Channel;
import com.tolimoli.pms.repository.ChannelRepository;
import com.tolimoli.pms.repository.RateRepository;
import com.tolimoli.pms.repository.RoomRepository;

import javax.transaction.Transactional;

// ===== 7. RATE SERVICE =====
@Service
@Transactional
public class RateService {

  @Autowired
  private RateRepository rateRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private ChannelRepository channelRepository;

  // Create/Update rate
  public Rate setRate(Long roomId, Long channelId, LocalDate date,
      BigDecimal rateAmount, Integer availableRooms) {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new RuntimeException("Room not found"));

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new RuntimeException("Channel not found"));

    // Check if rate already exists
    Optional<Rate> existingRate = rateRepository.findByRoomIdAndChannelIdAndRateDate(
        roomId, channelId, date);

    Rate rate;
    if (existingRate.isPresent()) {
      rate = existingRate.get();
      rate.updateRate(rateAmount);
      rate.updateAvailability(availableRooms);
    } else {
      rate = new Rate();
      rate.setRoom(room);
      rate.setChannel(channel);
      rate.setRateDate(date);
      rate.setRateAmount(rateAmount);
      rate.setAvailableRooms(availableRooms);
    }

    return rateRepository.save(rate);
  }

  // Get rates for room and date range
  public List<Rate> getRatesForRoom(Long roomId, LocalDate startDate, LocalDate endDate) {
    return rateRepository.findByRoomIdAndRateDateBetween(roomId, startDate, endDate);
  }

  // Get rates for channel
  public List<Rate> getRatesForChannel(Long channelId, LocalDate date) {
    return rateRepository.findByChannelIdAndRateDate(channelId, date);
  }

  // Update availability after booking
  public void updateAvailabilityAfterBooking(Long roomId, Long channelId,
      LocalDate checkIn, LocalDate checkOut) {
    LocalDate current = checkIn;

    while (current.isBefore(checkOut)) {
      Optional<Rate> rateOpt = rateRepository.findByRoomIdAndChannelIdAndRateDate(
          roomId, channelId, current);

      if (rateOpt.isPresent()) {
        Rate rate = rateOpt.get();
        if (rate.getAvailableRooms() > 0) {
          rate.updateAvailability(rate.getAvailableRooms() - 1);
          rateRepository.save(rate);
        }
      }

      current = current.plusDays(1);
    }
  }

  // Bulk update rates for multiple dates
  public void bulkUpdateRates(Long roomId, Long channelId, LocalDate startDate,
      LocalDate endDate, BigDecimal rateAmount, Integer availability) {
    LocalDate current = startDate;

    while (!current.isAfter(endDate)) {
      setRate(roomId, channelId, current, rateAmount, availability);
      current = current.plusDays(1);
    }
  }

  // Block/Unblock sales
  public void blockSales(Long roomId, Long channelId, LocalDate date) {
    Optional<Rate> rateOpt = rateRepository.findByRoomIdAndChannelIdAndRateDate(
        roomId, channelId, date);

    if (rateOpt.isPresent()) {
      Rate rate = rateOpt.get();
      rate.blockSales();
      rateRepository.save(rate);
    }
  }
}