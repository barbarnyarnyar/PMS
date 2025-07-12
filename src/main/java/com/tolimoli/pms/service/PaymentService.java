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
import com.tolimoli.pms.exception.ResourceNotFoundException;
import com.tolimoli.pms.exception.BusinessLogicException;
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
    
    // Validate input parameters
    if (reservationId == null) {
      throw new IllegalArgumentException("Reservation ID is required");
    }
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Payment amount must be greater than zero");
    }
    if (paymentMethod == null) {
      throw new IllegalArgumentException("Payment method is required");
    }
    
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));

    // Check if reservation is in a state that allows payment
    if (reservation.getStatus() == com.tolimoli.pms.entity.ReservationStatus.CANCELLED) {
      throw new BusinessLogicException("Cannot process payment for cancelled reservation");
    }

    // Check if payment amount is reasonable (not exceeding outstanding balance by too much)
    BigDecimal outstandingBalance = getOutstandingBalance(reservationId);
    if (amount.compareTo(outstandingBalance.multiply(new BigDecimal("2"))) > 0) {
      throw new BusinessLogicException("Payment amount (" + amount + 
          ") significantly exceeds outstanding balance (" + outstandingBalance + ")");
    }

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
    if (originalPaymentId == null) {
      throw new IllegalArgumentException("Original payment ID is required");
    }
    if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Refund amount must be greater than zero");
    }
    
    Payment originalPayment = paymentRepository.findById(originalPaymentId)
        .orElseThrow(() -> new ResourceNotFoundException("Payment", originalPaymentId));

    // Validate refund amount doesn't exceed original payment
    if (refundAmount.compareTo(originalPayment.getAmount()) > 0) {
      throw new BusinessLogicException("Refund amount (" + refundAmount + 
          ") cannot exceed original payment amount (" + originalPayment.getAmount() + ")");
    }

    // Check if original payment is successful
    if (originalPayment.getPaymentStatus() != PaymentStatus.COMPLETED) {
      throw new BusinessLogicException("Can only refund completed payments");
    }

    Payment refund = new Payment();
    refund.setReservation(originalPayment.getReservation());
    refund.setAmount(refundAmount.negate()); // Negative amount for refund
    refund.setPaymentMethod(originalPayment.getPaymentMethod());
    refund.setPaymentStatus(PaymentStatus.REFUNDED);
    refund.setTransactionId("REFUND-" + originalPayment.getTransactionId());
    refund.setPaymentDate(LocalDateTime.now());

    Payment savedRefund = paymentRepository.save(refund);

    // Update reservation paid amount
    Reservation reservation = originalPayment.getReservation();
    BigDecimal currentPaid = reservation.getPaidAmount();
    reservation.setPaidAmount(currentPaid.subtract(refundAmount));
    reservationRepository.save(reservation);

    return savedRefund;
  }
}