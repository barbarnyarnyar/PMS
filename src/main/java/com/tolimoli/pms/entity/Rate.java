package com.tolimoli.pms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// ===== RATE ENTITY =====
@Entity
@Table(name = "rates", uniqueConstraints = @UniqueConstraint(columnNames = { "room_id", "channel_id", "rate_date" }))
public class Rate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @Column(nullable = false)
  private LocalDate rateDate;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal rateAmount;

  @Column(nullable = false)
  private Integer availableRooms;

  @Column(precision = 10, scale = 2)
  private BigDecimal extraPersonRate; // Rate for extra person

  @Column(precision = 10, scale = 2)
  private BigDecimal childRate; // Rate for children

  private Boolean isBlocked = false; // Block sales for this date

  private String restrictions; // "MIN_STAY_2", "NO_ARRIVAL_SUNDAY"

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

  // Constructors
  public Rate() {
  }

  public Rate(Room room, Channel channel, LocalDate rateDate,
      BigDecimal rateAmount, Integer availableRooms) {
    this.room = room;
    this.channel = channel;
    this.rateDate = rateDate;
    this.rateAmount = rateAmount;
    this.availableRooms = availableRooms;
  }

  // Business Methods
  public boolean isValidForDate(LocalDate date) {
    return rateDate.equals(date) && !isBlocked && availableRooms > 0;
  }

  public boolean isAvailable() {
    return !isBlocked && availableRooms > 0;
  }

  public void updateAvailability(Integer rooms) {
    this.availableRooms = rooms;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateRate(BigDecimal newRate) {
    this.rateAmount = newRate;
    this.updatedAt = LocalDateTime.now();
  }

  public void blockSales() {
    this.isBlocked = true;
    this.updatedAt = LocalDateTime.now();
  }

  public void unblockSales() {
    this.isBlocked = false;
    this.updatedAt = LocalDateTime.now();
  }

  public BigDecimal calculateTotalRate(Integer numberOfGuests, Integer numberOfChildren) {
    BigDecimal total = rateAmount;

    // Add extra person charges
    if (numberOfGuests > room.getCapacity() && extraPersonRate != null) {
      int extraPersons = numberOfGuests - room.getCapacity();
      total = total.add(extraPersonRate.multiply(new BigDecimal(extraPersons)));
    }

    // Add child charges
    if (numberOfChildren > 0 && childRate != null) {
      total = total.add(childRate.multiply(new BigDecimal(numberOfChildren)));
    }

    return total;
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

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public LocalDate getRateDate() {
    return rateDate;
  }

  public void setRateDate(LocalDate rateDate) {
    this.rateDate = rateDate;
  }

  public BigDecimal getRateAmount() {
    return rateAmount;
  }

  public void setRateAmount(BigDecimal rateAmount) {
    this.rateAmount = rateAmount;
  }

  public Integer getAvailableRooms() {
    return availableRooms;
  }

  public void setAvailableRooms(Integer availableRooms) {
    this.availableRooms = availableRooms;
  }

  public BigDecimal getExtraPersonRate() {
    return extraPersonRate;
  }

  public void setExtraPersonRate(BigDecimal extraPersonRate) {
    this.extraPersonRate = extraPersonRate;
  }

  public BigDecimal getChildRate() {
    return childRate;
  }

  public void setChildRate(BigDecimal childRate) {
    this.childRate = childRate;
  }

  public Boolean getIsBlocked() {
    return isBlocked;
  }

  public void setIsBlocked(Boolean isBlocked) {
    this.isBlocked = isBlocked;
  }

  public String getRestrictions() {
    return restrictions;
  }

  public void setRestrictions(String restrictions) {
    this.restrictions = restrictions;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}