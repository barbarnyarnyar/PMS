package com.tolimoli.pms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// ===== PAYMENT ENTITY =====
@Entity
@Table(name = "payments")
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id", nullable = false)
  private Reservation reservation;

  @Column(nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus = PaymentStatus.PENDING;

  private String transactionId;
  private String cardLastFour;
  private LocalDateTime paymentDate = LocalDateTime.now();
  private LocalDateTime createdAt = LocalDateTime.now();

  // Constructors, getters, setters...
  public Payment() {
  }

  public Payment(Reservation reservation, BigDecimal amount, PaymentMethod paymentMethod) {
    this.reservation = reservation;
    this.amount = amount;
    this.paymentMethod = paymentMethod;
  }

  // Business methods
  public boolean isSuccessful() {
    return paymentStatus == PaymentStatus.COMPLETED;
  }

  public boolean isPending() {
    return paymentStatus == PaymentStatus.PENDING;
  }

  public boolean isFailed() {
    return paymentStatus == PaymentStatus.FAILED;
  }

  public boolean isRefunded() {
    return paymentStatus == PaymentStatus.REFUNDED;
  }
}

enum PaymentMethod {
  CREDIT_CARD, DEBIT_CARD, CASH, BANK_TRANSFER, CORPORATE_BILLING
}

enum PaymentStatus {
  PENDING, COMPLETED, FAILED, REFUNDED, CANCELLED
}
