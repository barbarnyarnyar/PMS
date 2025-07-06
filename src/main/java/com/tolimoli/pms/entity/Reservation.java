package com.tolimoli.pms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String confirmationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel; // Added channel relationship

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    private Integer numberOfGuests;
    private Integer numberOfChildren = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.CONFIRMED;

    @Column(length = 1000)
    private String specialRequests;

    @Column(length = 500)
    private String externalConfirmationNumber; // OTA confirmation number

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Audit fields
    @Column(length = 100)
    private String createdBy;

    @Column(length = 100)
    private String updatedBy;

    // Relationships
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FolioCharge> folioCharges;

    // Constructors
    public Reservation() {
    }

    public Reservation(String confirmationNumber, Guest guest, Room room, Channel channel,
            LocalDate checkInDate, LocalDate checkOutDate) {
        this.confirmationNumber = confirmationNumber;
        this.guest = guest;
        this.room = room;
        this.channel = channel;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // Business Methods
    public Long getDurationInDays() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public boolean isActive() {
        return status == ReservationStatus.CONFIRMED || status == ReservationStatus.CHECKED_IN;
    }

    public boolean canCheckIn() {
        return status == ReservationStatus.CONFIRMED &&
                !checkInDate.isAfter(LocalDate.now());
    }

    public boolean canCheckOut() {
        return status == ReservationStatus.CHECKED_IN;
    }

    public void checkIn() {
        if (!canCheckIn()) {
            throw new IllegalStateException("Cannot check in - invalid status or date");
        }
        this.status = ReservationStatus.CHECKED_IN;
        this.room.markAsOccupied();
        this.updatedAt = LocalDateTime.now();
    }

    public void checkOut() {
        if (!canCheckOut()) {
            throw new IllegalStateException("Cannot check out - not checked in");
        }
        this.status = ReservationStatus.CHECKED_OUT;
        this.room.markAsAvailable();
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status == ReservationStatus.CHECKED_OUT) {
            throw new IllegalStateException("Cannot cancel - already checked out");
        }
        this.status = ReservationStatus.CANCELLED;
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            this.room.markAsAvailable();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getOutstandingBalance() {
        if (totalAmount == null)
            return BigDecimal.ZERO;
        if (paidAmount == null)
            return totalAmount;
        return totalAmount.subtract(paidAmount);
    }

    public boolean isFullyPaid() {
        return getOutstandingBalance().compareTo(BigDecimal.ZERO) <= 0;
    }

    public BigDecimal getChannelCommission() {
        if (channel != null && totalAmount != null) {
            return channel.calculateCommission(totalAmount);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getNetRevenue() {
        if (totalAmount == null)
            return BigDecimal.ZERO;
        return totalAmount.subtract(getChannelCommission());
    }

    public boolean isFromOTA() {
        return channel != null && !channel.isDirect();
    }

    public boolean isOverdue() {
        return checkOutDate.isBefore(LocalDate.now()) &&
                getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0;
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

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
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

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getExternalConfirmationNumber() {
        return externalConfirmationNumber;
    }

    public void setExternalConfirmationNumber(String externalConfirmationNumber) {
        this.externalConfirmationNumber = externalConfirmationNumber;
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

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<FolioCharge> getFolioCharges() {
        return folioCharges;
    }

    public void setFolioCharges(List<FolioCharge> folioCharges) {
        this.folioCharges = folioCharges;
    }
}