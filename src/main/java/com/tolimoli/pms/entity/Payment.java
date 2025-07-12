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

  public LocalDateTime getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDateTime paymentDate) {
    this.paymentDate = paymentDate;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

    public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

    public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getCardLastFour() {
    return cardLastFour;
  }

  public void setCardLastFour(String cardLastFour) {
    this.cardLastFour = cardLastFour;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
  }
}