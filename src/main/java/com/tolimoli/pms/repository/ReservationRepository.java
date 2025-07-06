package com.tolimoli.pms.repository;

import com.tolimoli.pms.entity.Reservation;
import com.tolimoli.pms.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Reservation Repository with comprehensive query methods
 * 
 * Provides data access operations for Reservation entity including
 * custom queries for business operations, reporting, and analytics
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>,
    JpaSpecificationExecutor<Reservation> {

  // ===== BASIC FINDER METHODS =====

  /**
   * Find reservation by confirmation number
   */
  Optional<Reservation> findByConfirmationNumber(String confirmationNumber);

  /**
   * Find reservation by external confirmation number (OTA booking reference)
   */
  Optional<Reservation> findByExternalConfirmationNumber(String externalConfirmationNumber);

  /**
   * Check if confirmation number exists
   */
  boolean existsByConfirmationNumber(String confirmationNumber);

  // ===== GUEST-BASED QUERIES =====

  /**
   * Find all reservations for a specific guest
   */
  @Query("SELECT r FROM Reservation r WHERE r.guest.id = :guestId ORDER BY r.createdAt DESC")
  List<Reservation> findByGuestId(@Param("guestId") Long guestId);

  /**
   * Find active reservations for a guest
   */
  @Query("SELECT r FROM Reservation r WHERE r.guest.id = :guestId " +
      "AND r.status IN ('CONFIRMED', 'CHECKED_IN') ORDER BY r.checkInDate")
  List<Reservation> findActiveReservationsByGuestId(@Param("guestId") Long guestId);

  /**
   * Find guest's reservation history with pagination
   */
  @Query("SELECT r FROM Reservation r WHERE r.guest.email = :email ORDER BY r.createdAt DESC")
  Page<Reservation> findByGuestEmail(@Param("email") String email, Pageable pageable);

  // ===== ROOM-BASED QUERIES =====

  /**
   * Find reservations for a specific room
   */
  @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId ORDER BY r.checkInDate DESC")
  List<Reservation> findByRoomId(@Param("roomId") Long roomId);

  /**
   * Find active reservations for a room
   */
  @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
      "AND r.status IN ('CONFIRMED', 'CHECKED_IN') " +
      "ORDER BY r.checkInDate")
  List<Reservation> findActiveReservationsByRoomId(@Param("roomId") Long roomId);

  /**
   * Check if room is available for given dates
   */
  @Query("SELECT COUNT(r) FROM Reservation r WHERE r.room.id = :roomId " +
      "AND r.status IN ('CONFIRMED', 'CHECKED_IN') " +
      "AND ((r.checkInDate <= :checkOutDate AND r.checkOutDate > :checkInDate))")
  long countConflictingReservations(@Param("roomId") Long roomId,
      @Param("checkInDate") LocalDate checkInDate,
      @Param("checkOutDate") LocalDate checkOutDate);

  // ===== DATE-BASED QUERIES =====

  /**
   * Find reservations by check-in date
   */
  @Query("SELECT r FROM Reservation r WHERE r.checkInDate = :date ORDER BY r.createdAt")
  List<Reservation> findByCheckInDate(@Param("date") LocalDate date);

  /**
   * Find reservations by check-out date
   */
  @Query("SELECT r FROM Reservation r WHERE r.checkOutDate = :date ORDER BY r.createdAt")
  List<Reservation> findByCheckOutDate(@Param("date") LocalDate date);

  /**
   * Find arrivals for today
   */
  @Query("SELECT r FROM Reservation r WHERE r.checkInDate = CURRENT_DATE " +
      "AND r.status = 'CONFIRMED' ORDER BY r.createdAt")
  List<Reservation> findTodaysArrivals();

  /**
   * Find departures for today
   */
  @Query("SELECT r FROM Reservation r WHERE r.checkOutDate = CURRENT_DATE " +
      "AND r.status = 'CHECKED_IN' ORDER BY r.actualCheckInTime")
  List<Reservation> findTodaysDepartures();

  /**
   * Find reservations within date range
   */
  @Query("SELECT r FROM Reservation r WHERE " +
      "(r.checkInDate <= :endDate AND r.checkOutDate > :startDate) " +
      "ORDER BY r.checkInDate, r.createdAt")
  List<Reservation> findReservationsInDateRange(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  /**
   * Find in-house guests (currently checked in)
   */
  @Query("SELECT r FROM Reservation r WHERE r.status = 'CHECKED_IN' " +
      "ORDER BY r.actualCheckInTime")
  List<Reservation> findInHouseGuests();

  // ===== STATUS-BASED QUERIES =====

  /**
   * Find reservations by status
   */
  List<Reservation> findByStatus(ReservationStatus status);

  /**
   * Find reservations by multiple statuses
   */
  @Query("SELECT r FROM Reservation r WHERE r.status IN :statuses ORDER BY r.checkInDate")
  List<Reservation> findByStatusIn(@Param("statuses") List<ReservationStatus> statuses);

  /**
   * Find confirmed reservations for a date range
   */
  @Query("SELECT r FROM Reservation r WHERE r.status = 'CONFIRMED' " +
      "AND r.checkInDate >= :startDate AND r.checkInDate <= :endDate " +
      "ORDER BY r.checkInDate")
  List<Reservation> findConfirmedReservationsInRange(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  /**
   * Find overdue reservations (past checkout date but not checked out)
   */
  @Query("SELECT r FROM Reservation r WHERE r.checkOutDate < CURRENT_DATE " +
      "AND r.status IN ('CONFIRMED', 'CHECKED_IN') " +
      "ORDER BY r.checkOutDate")
  List<Reservation> findOverdueReservations();

  /**
   * Find no-show reservations
   */
  @Query("SELECT r FROM Reservation r WHERE r.status = 'CONFIRMED' " +
      "AND r.checkInDate < CURRENT_DATE " +
      "ORDER BY r.checkInDate")
  List<Reservation> findPotentialNoShows();

  // ===== CHANNEL-BASED QUERIES =====

  /**
   * Find reservations by channel
   */
  @Query("SELECT r FROM Reservation r WHERE r.channel.id = :channelId ORDER BY r.createdAt DESC")
  List<Reservation> findByChannelId(@Param("channelId") Long channelId);

  /**
   * Find reservations by channel code
   */
  @Query("SELECT r FROM Reservation r WHERE r.channel.channelCode = :channelCode " +
      "ORDER BY r.createdAt DESC")
  List<Reservation> findByChannelCode(@Param("channelCode") String channelCode);

  /**
   * Find OTA reservations (non-direct bookings)
   */
  @Query("SELECT r FROM Reservation r WHERE r.channel.channelCode != 'DIRECT' " +
      "ORDER BY r.createdAt DESC")
  List<Reservation> findOTAReservations();

  // ===== FINANCIAL QUERIES =====

  /**
   * Find reservations with outstanding balance
   */
  @Query("SELECT r FROM Reservation r WHERE r.totalAmount > r.paidAmount " +
      "ORDER BY r.checkOutDate")
  List<Reservation> findReservationsWithOutstandingBalance();

  /**
   * Find fully paid reservations
   */
  @Query("SELECT r FROM Reservation r WHERE r.totalAmount <= r.paidAmount " +
      "ORDER BY r.createdAt DESC")
  List<Reservation> findFullyPaidReservations();

  /**
   * Find reservations by amount range
   */
  @Query("SELECT r FROM Reservation r WHERE r.totalAmount BETWEEN :minAmount AND :maxAmount " +
      "ORDER BY r.totalAmount DESC")
  List<Reservation> findByAmountRange(@Param("minAmount") BigDecimal minAmount,
      @Param("maxAmount") BigDecimal maxAmount);

  // ===== REPORTING QUERIES =====

  /**
   * Get daily occupancy count
   */
  @Query("SELECT COUNT(r) FROM Reservation r WHERE r.checkInDate <= :date " +
      "AND r.checkOutDate > :date AND r.status IN ('CONFIRMED', 'CHECKED_IN')")
  long getDailyOccupancyCount(@Param("date") LocalDate date);

  /**
   * Get monthly revenue
   */
  @Query("SELECT SUM(r.totalAmount) FROM Reservation r WHERE " +
      "EXTRACT(YEAR FROM r.checkInDate) = :year AND " +
      "EXTRACT(MONTH FROM r.checkInDate) = :month AND " +
      "r.status != 'CANCELLED'")
  BigDecimal getMonthlyRevenue(@Param("year") int year, @Param("month") int month);

  /**
   * Get revenue by date range
   */
  @Query("SELECT SUM(r.totalAmount) FROM Reservation r WHERE " +
      "r.checkInDate >= :startDate AND r.checkInDate <= :endDate " +
      "AND r.status != 'CANCELLED'")
  BigDecimal getRevenueBetweenDates(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  /**
   * Get channel performance (revenue by channel)
   */
  @Query("SELECT r.channel.channelName, SUM(r.totalAmount), COUNT(r) " +
      "FROM Reservation r WHERE r.checkInDate >= :startDate AND r.checkInDate <= :endDate " +
      "AND r.status != 'CANCELLED' " +
      "GROUP BY r.channel.channelName ORDER BY SUM(r.totalAmount) DESC")
  List<Object[]> getChannelPerformance(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  /**
   * Get room type performance
   */
  @Query("SELECT r.room.roomType, COUNT(r), SUM(r.totalAmount) " +
      "FROM Reservation r WHERE r.checkInDate >= :startDate AND r.checkInDate <= :endDate " +
      "AND r.status != 'CANCELLED' " +
      "GROUP BY r.room.roomType ORDER BY COUNT(r) DESC")
  List<Object[]> getRoomTypePerformance(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  /**
   * Get average daily rate (ADR)
   */
  @Query("SELECT AVG(r.totalAmount / " +
      "(CAST(r.checkOutDate AS long) - CAST(r.checkInDate AS long))) " +
      "FROM Reservation r WHERE r.checkInDate >= :startDate AND r.checkInDate <= :endDate " +
      "AND r.status != 'CANCELLED'")
  BigDecimal getAverageDailyRate(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  // ===== SEARCH QUERIES =====

  /**
   * Search reservations by guest name or confirmation number
   */
  @Query("SELECT r FROM Reservation r WHERE " +
      "LOWER(CONCAT(r.guest.firstName, ' ', r.guest.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
      "OR LOWER(r.confirmationNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
      "ORDER BY r.createdAt DESC")
  List<Reservation> searchReservations(@Param("searchTerm") String searchTerm);

  /**
   * Advanced search with multiple criteria
   */
  @Query("SELECT r FROM Reservation r WHERE " +
      "(:guestName IS NULL OR LOWER(CONCAT(r.guest.firstName, ' ', r.guest.lastName)) LIKE LOWER(CONCAT('%', :guestName, '%'))) "
      +
      "AND (:email IS NULL OR LOWER(r.guest.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
      "AND (:roomNumber IS NULL OR r.room.roomNumber = :roomNumber) " +
      "AND (:status IS NULL OR r.status = :status) " +
      "AND (:channelCode IS NULL OR r.channel.channelCode = :channelCode) " +
      "AND (:checkInFrom IS NULL OR r.checkInDate >= :checkInFrom) " +
      "AND (:checkInTo IS NULL OR r.checkInDate <= :checkInTo) " +
      "ORDER BY r.checkInDate DESC")
  Page<Reservation> findWithFilters(@Param("guestName") String guestName,
      @Param("email") String email,
      @Param("roomNumber") String roomNumber,
      @Param("status") ReservationStatus status,
      @Param("channelCode") String channelCode,
      @Param("checkInFrom") LocalDate checkInFrom,
      @Param("checkInTo") LocalDate checkInTo,
      Pageable pageable);

  // ===== UPDATE OPERATIONS =====

  /**
   * Update reservation status
   */
  @Modifying
  @Query("UPDATE Reservation r SET r.status = :status, r.updatedAt = CURRENT_TIMESTAMP " +
      "WHERE r.id = :reservationId")
  int updateReservationStatus(@Param("reservationId") Long reservationId,
      @Param("status") ReservationStatus status);

  /**
   * Update paid amount
   */
  @Modifying
  @Query("UPDATE Reservation r SET r.paidAmount = :paidAmount, r.updatedAt = CURRENT_TIMESTAMP " +
      "WHERE r.id = :reservationId")
  int updatePaidAmount(@Param("reservationId") Long reservationId,
      @Param("paidAmount") BigDecimal paidAmount);

  /**
   * Mark as checked in
   */
  @Modifying
  @Query("UPDATE Reservation r SET r.status = 'CHECKED_IN', " +
      "r.actualCheckInTime = :checkInTime, r.updatedAt = CURRENT_TIMESTAMP " +
      "WHERE r.id = :reservationId AND r.status = 'CONFIRMED'")
  int markAsCheckedIn(@Param("reservationId") Long reservationId,
      @Param("checkInTime") LocalDateTime checkInTime);

  /**
   * Mark as checked out
   */
  @Modifying
  @Query("UPDATE Reservation r SET r.status = 'CHECKED_OUT', " +
      "r.actualCheckOutTime = :checkOutTime, r.updatedAt = CURRENT_TIMESTAMP " +
      "WHERE r.id = :reservationId AND r.status = 'CHECKED_IN'")
  int markAsCheckedOut(@Param("reservationId") Long reservationId,
      @Param("checkOutTime") LocalDateTime checkOutTime);

  /**
   * Cancel reservation
   */
  @Modifying
  @Query("UPDATE Reservation r SET r.status = 'CANCELLED', " +
      "r.cancellationDate = :cancellationDate, r.cancellationReason = :reason, " +
      "r.updatedAt = CURRENT_TIMESTAMP " +
      "WHERE r.id = :reservationId")
  int cancelReservation(@Param("reservationId") Long reservationId,
      @Param("cancellationDate") LocalDateTime cancellationDate,
      @Param("reason") String reason);

  // ===== STATISTICAL QUERIES =====

  /**
   * Get reservation count by status
   */
  @Query("SELECT r.status, COUNT(r) FROM Reservation r GROUP BY r.status")
  List<Object[]> getReservationCountByStatus();

  /**
   * Get monthly booking trends
   */
  @Query("SELECT EXTRACT(YEAR FROM r.createdAt), EXTRACT(MONTH FROM r.createdAt), COUNT(r) " +
      "FROM Reservation r WHERE r.createdAt >= :startDate " +
      "GROUP BY EXTRACT(YEAR FROM r.createdAt), EXTRACT(MONTH FROM r.createdAt) " +
      "ORDER BY EXTRACT(YEAR FROM r.createdAt), EXTRACT(MONTH FROM r.createdAt)")
  List<Object[]> getMonthlyBookingTrends(@Param("startDate") LocalDateTime startDate);

  /**
   * Get average length of stay
   */
  @Query("SELECT AVG(CAST(r.checkOutDate AS long) - CAST(r.checkInDate AS long)) " +
      "FROM Reservation r WHERE r.status != 'CANCELLED'")
  Double getAverageLengthOfStay();

  /**
   * Get top spending guests
   */
  @Query("SELECT r.guest.firstName, r.guest.lastName, r.guest.email, SUM(r.totalAmount) " +
      "FROM Reservation r WHERE r.status != 'CANCELLED' " +
      "GROUP BY r.guest.id, r.guest.firstName, r.guest.lastName, r.guest.email " +
      "ORDER BY SUM(r.totalAmount) DESC")
  List<Object[]> getTopSpendingGuests(Pageable pageable);

  // ===== BUSINESS LOGIC QUERIES =====

  /**
   * Find duplicate bookings (same guest, overlapping dates)
   */
  @Query("SELECT r1 FROM Reservation r1, Reservation r2 WHERE " +
      "r1.id != r2.id AND r1.guest.id = r2.guest.id " +
      "AND r1.status IN ('CONFIRMED', 'CHECKED_IN') " +
      "AND r2.status IN ('CONFIRMED', 'CHECKED_IN') " +
      "AND ((r1.checkInDate <= r2.checkOutDate AND r1.checkOutDate > r2.checkInDate))")
  List<Reservation> findDuplicateBookings();

  /**
   * Find reservations requiring early check-in
   */
  @Query("SELECT r FROM Reservation r WHERE r.checkInDate = :date " +
      "AND r.specialRequests LIKE '%early%check%in%' " +
      "ORDER BY r.createdAt")
  List<Reservation> findEarlyCheckInRequests(@Param("date") LocalDate date);

  /**
   * Find reservations requiring late check-out
   */
  @Query("SELECT r FROM Reservation r WHERE r.checkOutDate = :date " +
      "AND r.specialRequests LIKE '%late%check%out%' " +
      "ORDER BY r.actualCheckInTime")
  List<Reservation> findLateCheckOutRequests(@Param("date") LocalDate date);

  /**
   * Find VIP reservations
   */
  @Query("SELECT r FROM Reservation r WHERE r.guestType = 'VIP' " +
      "OR r.specialRequests LIKE '%VIP%' " +
      "ORDER BY r.checkInDate")
  List<Reservation> findVIPReservations();

  /**
   * Find group bookings
   */
  @Query("SELECT r FROM Reservation r WHERE r.isGroupBooking = true " +
      "ORDER BY r.checkInDate, r.groupName")
  List<Reservation> findGroupBookings();

  /**
   * Find reservations by group name
   */
  @Query("SELECT r FROM Reservation r WHERE r.groupName = :groupName " +
      "ORDER BY r.createdAt")
  List<Reservation> findByGroupName(@Param("groupName") String groupName);

  // ===== HOUSEKEEPING QUERIES =====

  /**
   * Find rooms that need cleaning (checkout today)
   */
  @Query("SELECT DISTINCT r.room FROM Reservation r WHERE r.checkOutDate = CURRENT_DATE " +
      "AND r.status = 'CHECKED_OUT'")
  List<Object> findRoomsNeedingCleaning();

  /**
   * Find rooms ready for next guest (checking in today)
   */
  @Query("SELECT r FROM Reservation r WHERE r.checkInDate = CURRENT_DATE " +
      "AND r.status = 'CONFIRMED' AND r.room.status = 'AVAILABLE' " +
      "ORDER BY r.createdAt")
  List<Reservation> findRoomsReadyForCheckIn();

  // ===== CUSTOM FINDER METHODS =====

  /**
   * Find recent reservations (last 30 days)
   */
  default List<Reservation> findRecentReservations() {
    LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
    return findReservationsInDateRange(thirtyDaysAgo, LocalDate.now());
  }

  /**
   * Find upcoming arrivals (next 7 days)
   */
  default List<Reservation> findUpcomingArrivals() {
    LocalDate today = LocalDate.now();
    LocalDate nextWeek = today.plusDays(7);
    return findConfirmedReservationsInRange(today, nextWeek);
  }

  /**
   * Check room availability for booking
   */
  default boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
    return countConflictingReservations(roomId, checkIn, checkOut) == 0;
  }
}