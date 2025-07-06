package com.tolimoli.pms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;

// ===== FOLIO CHARGE ENTITY =====
@Entity
@Table(name = "folio_charges")
public class FolioCharge {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id", nullable = false)
  private Reservation reservation;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ChargeType chargeType;

  @Column(nullable = false, length = 200)
  private String description;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal amount;

  @Column(nullable = false)
  private LocalDate chargeDate;

  @Column(nullable = false)
  private Boolean isPaid = false;

  @Column(length = 50)
  private String department; // "FRONT_OFFICE", "RESTAURANT", "SPA"

  @Column(length = 100)
  private String reference; // External reference like POS transaction ID

  private Integer quantity = 1; // For items like "2 x Mini Bar Beer"

  @Column(precision = 10, scale = 2)
  private BigDecimal unitPrice; // Price per unit

  @Column(precision = 5, scale = 2)
  private BigDecimal taxRate; // Tax percentage

  @Column(precision = 10, scale = 2)
  private BigDecimal taxAmount; // Calculated tax amount

  @Column(length = 1000)
  private String notes; // Additional notes

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

  // Audit fields
  @Column(length = 100)
  private String createdBy; // Staff member who added the charge

  @Column(length = 100)
  private String updatedBy; // Staff member who last updated

  // Constructors
  public FolioCharge() {
  }

  public FolioCharge(Reservation reservation, ChargeType chargeType,
      String description, BigDecimal amount, LocalDate chargeDate) {
    this.reservation = reservation;
    this.chargeType = chargeType;
    this.description = description;
    this.amount = amount;
    this.chargeDate = chargeDate;
  }

  public FolioCharge(Reservation reservation, ChargeType chargeType,
      String description, BigDecimal unitPrice, Integer quantity, LocalDate chargeDate) {
    this.reservation = reservation;
    this.chargeType = chargeType;
    this.description = description;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.amount = unitPrice.multiply(new BigDecimal(quantity));
    this.chargeDate = chargeDate;
  }

  // Business Methods
  public void markAsPaid() {
    this.isPaid = true;
    this.updatedAt = LocalDateTime.now();
  }

  public void markAsUnpaid() {
    this.isPaid = false;
    this.updatedAt = LocalDateTime.now();
  }

  public boolean isRoomCharge() {
    return chargeType == ChargeType.ROOM;
  }

  public boolean isTaxCharge() {
    return chargeType == ChargeType.TAX;
  }

  public boolean isRefundable() {
    // Business logic: Room charges are typically non-refundable after check-in
    if (isRoomCharge()) {
      return reservation.getStatus() == ReservationStatus.CONFIRMED;
    }
    // Service charges are usually refundable within 24 hours
    return chargeDate.isAfter(LocalDate.now().minusDays(1));
  }

  public BigDecimal getAmountWithTax() {
    if (taxAmount != null) {
      return amount.add(taxAmount);
    }
    return amount;
  }

  public void calculateTax() {
    if (taxRate != null && amount != null) {
      this.taxAmount = amount.multiply(taxRate).divide(new BigDecimal("100"));
    }
  }

  public void updateAmount() {
    if (unitPrice != null && quantity != null) {
      this.amount = unitPrice.multiply(new BigDecimal(quantity));
      calculateTax();
      this.updatedAt = LocalDateTime.now();
    }
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
  }

  public ChargeType getChargeType() {
    return chargeType;
  }

  public void setChargeType(ChargeType chargeType) {
    this.chargeType = chargeType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDate getChargeDate() {
    return chargeDate;
  }

  public void setChargeDate(LocalDate chargeDate) {
    this.chargeDate = chargeDate;
  }

  public Boolean getIsPaid() {
    return isPaid;
  }

  public void setIsPaid(Boolean isPaid) {
    this.isPaid = isPaid;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
    updateAmount();
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
    updateAmount();
  }

  public BigDecimal getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BigDecimal taxRate) {
    this.taxRate = taxRate;
    calculateTax();
  }

  public BigDecimal getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}

// ===== ENUMS =====
enum ChargeType {
  ROOM("Room Accommodation"),
  TAX("Taxes and Fees"), 
  SERVICE("Hotel Services"),
  EXTRA("Additional Amenities"),
  PENALTY("Penalties and Fines"),
  DISCOUNT("Discounts and Promotions");
  
  private final String description;
  
  ChargeType(String description) {
    this.description = description;
  }
  
  public String getDescription() {
    return description;
  }
}