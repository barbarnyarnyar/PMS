package com.tolimoli.pms.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tolimoli.pms.entity.Payment;
import com.tolimoli.pms.entity.PaymentMethod;
import com.tolimoli.pms.entity.PaymentStatus;
import com.tolimoli.pms.entity.Reservation;
import com.tolimoli.pms.repository.PaymentRepository;
import com.tolimoli.pms.repository.ReservationRepository;

import jakarta.transaction.Transactional;

// ===== 6. PAYMENT SERVICE =====
@Service
@Transactional
public class PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private FolioChargeService folioChargeService;

  // Process payment
  public Payment processPayment(Long reservationId, BigDecimal amount,
      PaymentMethod paymentMethod, String transactionId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

    Payment payment = new Payment();
    payment.setReservation(reservation);
    payment.setAmount(amount);
    payment.setPaymentMethod(paymentMethod);
    payment.setPaymentStatus(PaymentStatus.COMPLETED);
    payment.setTransactionId(transactionId);
    payment.setPaymentDate(LocalDateTime.now());

    Payment savedPayment = paymentRepository.save(payment);

    // Update reservation paid amount
    BigDecimal currentPaid = reservation.getPaidAmount();
    reservation.setPaidAmount(currentPaid.add(amount));
    reservationRepository.save(reservation);

    // Check if fully paid and mark charges as paid
    BigDecimal totalCharges = folioChargeService.calculateTotalCharges(reservationId);
    if (reservation.getPaidAmount().compareTo(totalCharges) >= 0) {
      folioChargeService.markChargesAsPaid(reservationId);
    }

    return savedPayment;
  }

  // Get all payments for reservation
  public List<Payment> getReservationPayments(Long reservationId) {
    return paymentRepository.findByReservationId(reservationId);
  }

  // Calculate total payments
  public BigDecimal calculateTotalPayments(Long reservationId) {
    List<Payment> payments = getReservationPayments(reservationId);
    return payments.stream()
        .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
        .map(Payment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  // Get outstanding balance
  public BigDecimal getOutstandingBalance(Long reservationId) {
    BigDecimal totalCharges = folioChargeService.calculateTotalCharges(reservationId);
    BigDecimal totalPayments = calculateTotalPayments(reservationId);
    return totalCharges.subtract(totalPayments);
  }

  // Process refund
  public Payment processRefund(Long originalPaymentId, BigDecimal refundAmount) {
    Payment originalPayment = paymentRepository.findById(originalPaymentId)
        .orElseThrow(() -> new RuntimeException("Original payment not found"));

    Payment refund = new Payment();
    refund.setReservation(originalPayment.getReservation());
    refund.setAmount(refundAmount.negate()); // Negative amount for refund
    refund.setPaymentMethod(originalPayment.getPaymentMethod());
    refund.setPaymentStatus(PaymentStatus.REFUNDED);
    refund.setTransactionId("REFUND-" + originalPayment.getTransactionId());
    refund.setPaymentDate(LocalDateTime.now());

    return paymentRepository.save(refund);
  }
}