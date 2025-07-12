package com.tolimoli.pms.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tolimoli.pms.entity.ChargeType;
import com.tolimoli.pms.entity.FolioCharge;
import com.tolimoli.pms.entity.Reservation;
import com.tolimoli.pms.repository.FolioChargeRepository;
import com.tolimoli.pms.repository.ReservationRepository;

import javax.transaction.Transactional;

// ===== 5. FOLIO CHARGE SERVICE =====
@Service
@Transactional
public class FolioChargeService {

  @Autowired
  private FolioChargeRepository folioChargeRepository;

  @Autowired
  private ReservationRepository reservationRepository;

  // Create room charges automatically
  public void createRoomCharges(Reservation reservation) {
    LocalDate current = reservation.getCheckInDate();
    LocalDate end = reservation.getCheckOutDate();
    int nightNumber = 1;

    while (current.isBefore(end)) {
      FolioCharge roomCharge = new FolioCharge();
      roomCharge.setReservation(reservation);
      roomCharge.setChargeType(ChargeType.ROOM);
      roomCharge.setDescription("Room " + reservation.getRoom().getRoomNumber() +
          " - Night " + nightNumber);
      roomCharge.setAmount(reservation.getRoom().getBaseRate());
      roomCharge.setChargeDate(current);
      roomCharge.setDepartment("FRONT_OFFICE");

      folioChargeRepository.save(roomCharge);

      current = current.plusDays(1);
      nightNumber++;
    }
  }

  // Add service charge
  public FolioCharge addServiceCharge(Long reservationId, String description,
      BigDecimal amount, ChargeType chargeType) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

    FolioCharge charge = new FolioCharge();
    charge.setReservation(reservation);
    charge.setChargeType(chargeType);
    charge.setDescription(description);
    charge.setAmount(amount);
    charge.setChargeDate(LocalDate.now());

    return folioChargeRepository.save(charge);
  }

  // Add charge with quantity
  public FolioCharge addChargeWithQuantity(Long reservationId, String description,
      BigDecimal unitPrice, Integer quantity,
      ChargeType chargeType) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

    FolioCharge charge = new FolioCharge();
    charge.setReservation(reservation);
    charge.setChargeType(chargeType);
    charge.setDescription(description);
    charge.setUnitPrice(unitPrice);
    charge.setQuantity(quantity);
    charge.setAmount(unitPrice.multiply(new BigDecimal(quantity)));
    charge.setChargeDate(LocalDate.now());

    return folioChargeRepository.save(charge);
  }

  // Get all charges for reservation
  public List<FolioCharge> getReservationCharges(Long reservationId) {
    return folioChargeRepository.findByReservationId(reservationId);
  }

  // Calculate total charges
  public BigDecimal calculateTotalCharges(Long reservationId) {
    List<FolioCharge> charges = getReservationCharges(reservationId);
    return charges.stream()
        .map(FolioCharge::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  // Mark charges as paid
  public void markChargesAsPaid(Long reservationId) {
    List<FolioCharge> charges = folioChargeRepository.findByReservationIdAndIsPaidFalse(reservationId);
    charges.forEach(charge -> charge.markAsPaid());
    folioChargeRepository.saveAll(charges);
  }
}