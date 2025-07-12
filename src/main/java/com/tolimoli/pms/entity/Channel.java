package com.tolimoli.pms.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// ===== CHANNEL ENTITY =====
@Entity
@Table(name = "channels")
public class Channel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 100)
  private String channelName; // "DIRECT", "BOOKING_COM", "EXPEDIA"

  @Column(unique = true, nullable = false, length = 10)
  private String channelCode; // "DIRECT", "BCM", "EXP"

  @Column(nullable = false)
  private Boolean isActive = true;

  @Column(precision = 5, scale = 2)
  private BigDecimal commissionRate; // 15.00 for 15%

  @Column(length = 500)
  private String apiEndpoint; // API URL for integration

  @Column(length = 500)
  private String apiCredentials; // Encrypted API keys/credentials

  @Column(length = 1000)
  private String description; // "Booking.com - Leading OTA"

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

  // Relationships
  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Rate> rates;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Reservation> reservations;

  // Constructors
  public Channel() {
  }

  public Channel(String channelName, String channelCode, BigDecimal commissionRate) {
    this.channelName = channelName;
    this.channelCode = channelCode;
    this.commissionRate = commissionRate;
  }

  // Business Methods
  public BigDecimal calculateCommission(BigDecimal amount) {
    if (commissionRate == null || amount == null) {
      return BigDecimal.ZERO;
    }
    return amount.multiply(commissionRate).divide(new BigDecimal("100"));
  }

  public boolean isOnlineChannel() {
    return apiEndpoint != null && !apiEndpoint.trim().isEmpty();
  }

  public boolean isDirect() {
    return "DIRECT".equalsIgnoreCase(channelCode);
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

  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  public String getChannelCode() {
    return channelCode;
  }

  public void setChannelCode(String channelCode) {
    this.channelCode = channelCode;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public BigDecimal getCommissionRate() {
    return commissionRate;
  }

  public void setCommissionRate(BigDecimal commissionRate) {
    this.commissionRate = commissionRate;
  }

  public String getApiEndpoint() {
    return apiEndpoint;
  }

  public void setApiEndpoint(String apiEndpoint) {
    this.apiEndpoint = apiEndpoint;
  }

  public String getApiCredentials() {
    return apiCredentials;
  }

  public void setApiCredentials(String apiCredentials) {
    this.apiCredentials = apiCredentials;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public List<Rate> getRates() {
    return rates;
  }

  public void setRates(List<Rate> rates) {
    this.rates = rates;
  }

  public List<Reservation> getReservations() {
    return reservations;
  }

  public void setReservations(List<Reservation> reservations) {
    this.reservations = reservations;
  }
}