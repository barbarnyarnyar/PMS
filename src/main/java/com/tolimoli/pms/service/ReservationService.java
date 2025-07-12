package com.tolimoli.pms.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tolimoli.pms.entity.Guest;
import com.tolimoli.pms.entity.Reservation;
import com.tolimoli.pms.entity.Room;
import com.tolimoli.pms.entity.Channel;
import com.tolimoli.pms.entity.RoomType;
import com.tolimoli.pms.entity.ReservationStatus;
import com.tolimoli.pms.exception.ResourceNotFoundException;
import com.tolimoli.pms.exception.BusinessLogicException;
import com.tolimoli.pms.repository.ReservationRepository;

import javax.transaction.Transactional;

// ===== 4. RESERVATION SERVICE =====
@Service
@Transactional
public class ReservationService {

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private RoomService roomService;

  @Autowired
  private GuestService guestService;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private FolioChargeService folioChargeService;

  // Create reservation
  public Reservation createReservation(String guestEmail, String roomNumber, String channelCode,
      LocalDate checkInDate, LocalDate checkOutDate,
      Integer numberOfGuests, String specialRequests) {

    // Validate input parameters
    if (guestEmail == null || guestEmail.trim().isEmpty()) {
      throw new IllegalArgumentException("Guest email is required");
    }
    if (roomNumber == null || roomNumber.trim().isEmpty()) {
      throw new IllegalArgumentException("Room number is required");
    }
    if (channelCode == null || channelCode.trim().isEmpty()) {
      throw new IllegalArgumentException("Channel code is required");
    }
    if (checkInDate == null || checkOutDate == null) {
      throw new IllegalArgumentException("Check-in and check-out dates are required");
    }
    if (!checkOutDate.isAfter(checkInDate)) {
      throw new IllegalArgumentException("Check-out date must be after check-in date");
    }
    if (numberOfGuests == null || numberOfGuests <= 0) {
      throw new IllegalArgumentException("Number of guests must be greater than 0");
    }

    // Get guest
    Guest guest = guestService.findGuestByEmail(guestEmail)
        .orElseThrow(() -> new ResourceNotFoundException("Guest", "email", guestEmail));

    // Get room
    Room room = roomService.getRoomByNumber(roomNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Room", "roomNumber", roomNumber));

    // Get channel
    Channel channel = channelService.getChannelByCode(channelCode)
        .orElseThrow(() -> new ResourceNotFoundException("Channel", "channelCode", channelCode));

    // Check room availability
    if (!room.isAvailable()) {
      throw new BusinessLogicException("Room " + roomNumber + " is not available");
    }

    // Check room capacity
    if (numberOfGuests > room.getCapacity()) {
      throw new BusinessLogicException("Number of guests (" + numberOfGuests + 
          ") exceeds room capacity (" + room.getCapacity() + ")");
    }

    // Check for conflicting reservations
    if (!reservationRepository.isRoomAvailable(room.getId(), checkInDate, checkOutDate)) {
      throw new BusinessLogicException("Room " + roomNumber + " is not available for the selected dates");
    }

    // Generate unique confirmation number
    String confirmationNumber = generateConfirmationNumber();
    while (reservationRepository.existsByConfirmationNumber(confirmationNumber)) {
      confirmationNumber = generateConfirmationNumber();
    }

    // Create reservation
    Reservation reservation = new Reservation();
    reservation.setConfirmationNumber(confirmationNumber);
    reservation.setGuest(guest);
    reservation.setRoom(room);
    reservation.setChannel(channel);
    reservation.setCheckInDate(checkInDate);
    reservation.setCheckOutDate(checkOutDate);
    reservation.setNumberOfGuests(numberOfGuests);
    reservation.setSpecialRequests(specialRequests);
    reservation.setStatus(ReservationStatus.CONFIRMED);

    // Calculate total amount
    Long nights = reservation.getDurationInDays();
    if (nights <= 0) {
      throw new BusinessLogicException("Invalid stay duration: " + nights + " nights");
    }
    BigDecimal totalAmount = room.getBaseRate().multiply(new BigDecimal(nights));
    reservation.setTotalAmount(totalAmount);

    Reservation savedReservation = reservationRepository.save(reservation);

    // Auto-create room charges
    folioChargeService.createRoomCharges(savedReservation);

    return savedReservation;
  }

  // Check-in guest
  public Reservation checkInGuest(Long reservationId) {
    if (reservationId == null) {
      throw new IllegalArgumentException("Reservation ID is required");
    }
    
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));

    if (!reservation.canCheckIn()) {
      throw new BusinessLogicException("Cannot check in reservation " + 
          reservation.getConfirmationNumber() + " - invalid status or date");
    }

    reservation.checkIn();
    return reservationRepository.save(reservation);
  }

  // Check-out guest
  public Reservation checkOutGuest(Long reservationId) {
    if (reservationId == null) {
      throw new IllegalArgumentException("Reservation ID is required");
    }
    
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));

    if (!reservation.canCheckOut()) {
      throw new BusinessLogicException("Cannot check out reservation " + 
          reservation.getConfirmationNumber() + " - guest is not checked in");
    }

    reservation.checkOut();
    return reservationRepository.save(reservation);
  }

  // Check availability
  public List<Room> checkAvailability(LocalDate checkIn, LocalDate checkOut, RoomType roomType) {
    return roomService.getAvailableRooms(checkIn, checkOut, roomType);
  }

  // Get reservation by confirmation number
  public Optional<Reservation> getReservationByConfirmation(String confirmationNumber) {
    return reservationRepository.findByConfirmationNumber(confirmationNumber);
  }

  private String generateConfirmationNumber() {
    // Generate a more readable confirmation number format: RES-YYYYMMDD-XXXXX
    java.time.LocalDate today = LocalDate.now();
    String dateStr = today.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
    String randomPart = String.format("%05d", (int)(Math.random() * 100000));
    return "RES-" + dateStr + "-" + randomPart;
  }
}