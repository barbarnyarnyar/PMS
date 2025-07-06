package com.tolimoli.pms.service;

import com.tolimoli.pms.entity.*;
import com.tolimoli.pms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// ===== 1. ROOM SERVICE =====
@Service
@Transactional
public class RoomService {

  @Autowired
  private RoomRepository roomRepository;

  // Create room
  public Room createRoom(String roomNumber, RoomType roomType, Integer capacity, BigDecimal baseRate) {
    Room room = new Room();
    room.setRoomNumber(roomNumber);
    room.setRoomType(roomType);
    room.setCapacity(capacity);
    room.setBaseRate(baseRate);
    room.setStatus(RoomStatus.AVAILABLE);
    room.setIsActive(true);

    return roomRepository.save(room);
  }

  // Get all rooms
  public List<Room> getAllRooms() {
    return roomRepository.findAll();
  }

  // Get available rooms for dates
  public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, RoomType roomType) {
    return roomRepository.findAvailableRooms(checkIn, checkOut, roomType);
  }

  // Update room status
  public Room updateRoomStatus(Long roomId, RoomStatus status) {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new RuntimeException("Room not found"));
    room.setStatus(status);
    return roomRepository.save(room);
  }

  // Get room by number
  public Optional<Room> getRoomByNumber(String roomNumber) {
    return roomRepository.findByRoomNumber(roomNumber);
  }
}