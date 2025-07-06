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
import com.tolimoli.pms.repository.ReservationRepository;

import jakarta.transaction.Transactional;

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

    // Get guest
    Guest guest = guestService.findGuestByEmail(guestEmail)
        .orElseThrow(() -> new RuntimeException("Guest not found"));

    // Get room
    Room room = roomService.getRoomByNumber(roomNumber)
        .orElseThrow(() -> new RuntimeException("Room not found"));

    // Get channel
    Channel channel = channelService.getChannelByCode(channelCode)
        .orElseThrow(() -> new RuntimeException("Channel not found"));

    // Check availability
    if (!room.isAvailable()) {
      throw new RuntimeException("Room not available");
    }

    // Generate confirmation number
    String confirmationNumber = generateConfirmationNumber();

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
    BigDecimal totalAmount = room.getBaseRate().multiply(new BigDecimal(nights));
    reservation.setTotalAmount(totalAmount);

    Reservation savedReservation = reservationRepository.save(reservation);

    // Auto-create room charges
    folioChargeService.createRoomCharges(savedReservation);

    return savedReservation;
  }

  // Check-in guest
  public Reservation checkInGuest(Long reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

    reservation.checkIn();
    return reservationRepository.save(reservation);
  }

  // Check-out guest
  public Reservation checkOutGuest(Long reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

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
    return "RES" + System.currentTimeMillis();
  }
}