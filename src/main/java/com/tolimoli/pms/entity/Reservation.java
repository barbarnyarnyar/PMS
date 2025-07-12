package com.tolimoli.pms.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Reservation Entity - Core booking information
 * 
 * Represents a guest's booking including room, dates, pricing, and status
 */
@Entity
@Table(name = "reservations", indexes = {
    @Index(name = "idx_reservation_confirmation", columnList = "confirmation_number"),
    @Index(name = "idx_reservation_guest", columnList = "guest_id"),
    @Index(name = "idx_reservation_room", columnList = "room_id"),
    @Index(name = "idx_reservation_dates", columnList = "check_in_date, check_out_date"),
    @Index(name = "idx_reservation_status", columnList = "status"),
    @Index(name = "idx_reservation_channel", columnList = "channel_id")
})
public class Reservation {
    
    // ===== PRIMARY KEY =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;
    
    // ===== BASIC INFORMATION =====
    @Column(name = "confirmation_number", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Confirmation number is required")
    @Size(max = 50, message = "Confirmation number cannot exceed 50 characters")
    private String confirmationNumber;
    
    @Column(name = "external_confirmation_number", length = 100)
    @Size(max = 100, message = "External confirmation number cannot exceed 100 characters")
    private String externalConfirmationNumber; // OTA confirmation number
    
    // ===== RELATIONSHIPS =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_reservation_guest"))
    @NotNull(message = "Guest is required")
    private Guest guest;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_reservation_room"))
    @NotNull(message = "Room is required")
    private Room room;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_reservation_channel"))
    @NotNull(message = "Channel is required")
    private Channel channel;
    
    // ===== DATE INFORMATION =====
    @Column(name = "check_in_date", nullable = false)
    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;
    
    @Column(name = "check_out_date", nullable = false)
    @NotNull(message = "Check-out date is required")
    private LocalDate checkOutDate;
    
    @Column(name = "actual_check_in_time")
    private LocalDateTime actualCheckInTime;
    
    @Column(name = "actual_check_out_time")
    private LocalDateTime actualCheckOutTime;
    
    // ===== GUEST INFORMATION =====
    @Column(name = "number_of_guests", nullable = false)
    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "At least 1 guest is required")
    @Max(value = 10, message = "Maximum 10 guests allowed")
    private Integer numberOfGuests;
    
    @Column(name = "number_of_children")
    @Min(value = 0, message = "Number of children cannot be negative")
    @Max(value = 8, message = "Maximum 8 children allowed")
    private Integer numberOfChildren = 0;
    
    @Column(name = "number_of_infants")
    @Min(value = 0, message = "Number of infants cannot be negative")
    @Max(value = 5, message = "Maximum 5 infants allowed")
    private Integer numberOfInfants = 0;
    
    // ===== FINANCIAL INFORMATION =====
    @Column(name = "total_amount", precision = 12, scale = 2)
    @DecimalMin(value = "0.0", message = "Total amount cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid total amount format")
    private BigDecimal totalAmount;
    
    @Column(name = "paid_amount", precision = 12, scale = 2, nullable = false)
    @DecimalMin(value = "0.0", message = "Paid amount cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid paid amount format")
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 12, scale = 2)
    @DecimalMin(value = "0.0", message = "Discount amount cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid discount amount format")
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 12, scale = 2)
    @DecimalMin(value = "0.0", message = "Tax amount cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid tax amount format")
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    // ===== STATUS AND CLASSIFICATION =====
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Reservation status is required")
    private ReservationStatus status = ReservationStatus.CONFIRMED;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "guest_type", length = 20)
    private GuestType guestType = GuestType.INDIVIDUAL;
    
    @Column(name = "is_group_booking", nullable = false)
    private Boolean isGroupBooking = false;
    
    @Column(name = "group_name", length = 100)
    @Size(max = 100, message = "Group name cannot exceed 100 characters")
    private String groupName;
    
    // ===== ADDITIONAL INFORMATION =====
    @Column(name = "special_requests", length = 1000)
    @Size(max = 1000, message = "Special requests cannot exceed 1000 characters")
    private String specialRequests;
    
    @Column(name = "internal_notes", length = 1000)
    @Size(max = 1000, message = "Internal notes cannot exceed 1000 characters")
    private String internalNotes;
    
    @Column(name = "cancellation_reason", length = 500)
    @Size(max = 500, message = "Cancellation reason cannot exceed 500 characters")
    private String cancellationReason;
    
    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;
    
    // ===== MARKETING AND PREFERENCES =====
    @Column(name = "rate_plan_code", length = 50)
    @Size(max = 50, message = "Rate plan code cannot exceed 50 characters")
    private String ratePlanCode;
    
    @Column(name = "promotion_code", length = 50)
    @Size(max = 50, message = "Promotion code cannot exceed 50 characters")
    private String promotionCode;
    
    @Column(name = "market_segment", length = 50)
    @Size(max = 50, message = "Market segment cannot exceed 50 characters")
    private String marketSegment; // CORPORATE, LEISURE, GROUP, etc.
    
    // ===== AUDIT FIELDS =====
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by", length = 100)
    @Size(max = 100, message = "Created by cannot exceed 100 characters")
    private String createdBy;
    
    @Column(name = "updated_by", length = 100)
    @Size(max = 100, message = "Updated by cannot exceed 100 characters")
    private String updatedBy;
    
    @Version
    @Column(name = "version")
    private Long version;
    
    // ===== CHILD ENTITIES =====
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, 
               fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();
    
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, 
               fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FolioCharge> folioCharges = new ArrayList<>();
    
    // ===== CONSTRUCTORS =====
    public Reservation() {}
    
    public Reservation(String confirmationNumber, Guest guest, Room room, Channel channel,
                      LocalDate checkInDate, LocalDate checkOutDate, Integer numberOfGuests) {
        this.confirmationNumber = confirmationNumber;
        this.guest = guest;
        this.room = room;
        this.channel = channel;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.status = ReservationStatus.CONFIRMED;
    }
    
    // ===== BUSINESS METHODS =====
    
    /**
     * Calculate the number of nights for this reservation
     */
    public Long getDurationInDays() {
        if (checkInDate == null || checkOutDate == null) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    /**
     * Check if reservation is active (confirmed or checked in)
     */
    public boolean isActive() {
        return status == ReservationStatus.CONFIRMED || status == ReservationStatus.CHECKED_IN;
    }
    
    /**
     * Check if guest can check in today
     */
    public boolean canCheckIn() {
        return status == ReservationStatus.CONFIRMED && 
               checkInDate != null &&
               !checkInDate.isAfter(LocalDate.now());
    }
    
    /**
     * Check if guest can check out
     */
    public boolean canCheckOut() {
        return status == ReservationStatus.CHECKED_IN;
    }
    
    /**
     * Perform check-in operation
     */
    public void checkIn() {
        if (!canCheckIn()) {
            throw new IllegalStateException("Cannot check in - invalid status or date");
        }
        
        this.status = ReservationStatus.CHECKED_IN;
        this.actualCheckInTime = LocalDateTime.now();
        
        // Update room status
        if (room != null) {
            room.setStatus(RoomStatus.OCCUPIED);
        }
    }
    
    /**
     * Perform check-out operation
     */
    public void checkOut() {
        if (!canCheckOut()) {
            throw new IllegalStateException("Cannot check out - not checked in");
        }
        
        this.status = ReservationStatus.CHECKED_OUT;
        this.actualCheckOutTime = LocalDateTime.now();
        
        // Update room status
        if (room != null) {
            room.setStatus(RoomStatus.DIRTY);
        }
    }
    
    /**
     * Cancel the reservation
     */
    public void cancel(String reason) {
        if (status == ReservationStatus.CHECKED_OUT) {
            throw new IllegalStateException("Cannot cancel - already checked out");
        }
        
        this.status = ReservationStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancellationDate = LocalDateTime.now();
        
        // Free up the room if it was occupied
        if (room != null && room.getStatus() == RoomStatus.OCCUPIED) {
            room.setStatus(RoomStatus.AVAILABLE);
        }
    }
    
    /**
     * Calculate outstanding balance
     */
    public BigDecimal getOutstandingBalance() {
        if (totalAmount == null) return BigDecimal.ZERO;
        if (paidAmount == null) return totalAmount;
        return totalAmount.subtract(paidAmount);
    }
    
    /**
     * Check if reservation is fully paid
     */
    public boolean isFullyPaid() {
        return getOutstandingBalance().compareTo(BigDecimal.ZERO) <= 0;
    }
    
    /**
     * Calculate channel commission
     */
    public BigDecimal getChannelCommission() {
        if (channel != null && totalAmount != null) {
            return channel.calculateCommission(totalAmount);
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Calculate net revenue after commission
     */
    public BigDecimal getNetRevenue() {
        if (totalAmount == null) return BigDecimal.ZERO;
        return totalAmount.subtract(getChannelCommission());
    }
    
    /**
     * Check if booking came from OTA
     */
    public boolean isFromOTA() {
        return channel != null && !channel.isDirect();
    }
    
    /**
     * Check if reservation is overdue
     */
    public boolean isOverdue() {
        return checkOutDate != null && 
               checkOutDate.isBefore(LocalDate.now()) && 
               getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Check if reservation is for today
     */
    public boolean isArrivingToday() {
        return checkInDate != null && checkInDate.equals(LocalDate.now());
    }
    
    /**
     * Check if reservation is departing today
     */
    public boolean isDepartingToday() {
        return checkOutDate != null && checkOutDate.equals(LocalDate.now());
    }
    
    /**
     * Calculate room rate per night
     */
    public BigDecimal getRatePerNight() {
        Long nights = getDurationInDays();
        if (nights > 0 && totalAmount != null) {
            return totalAmount.divide(new BigDecimal(nights), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Add payment to this reservation
     */
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setReservation(this);
        
        // Update paid amount
        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            this.paidAmount = this.paidAmount.add(payment.getAmount());
        }
    }
    
    /**
     * Add folio charge to this reservation
     */
    public void addFolioCharge(FolioCharge charge) {
        folioCharges.add(charge);
        charge.setReservation(this);
    }
    
    // ===== LIFECYCLE CALLBACKS =====
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        
        // Validate check-out date is after check-in date
        if (checkInDate != null && checkOutDate != null && 
            !checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        // Ensure paid amount doesn't exceed total amount
        if (paidAmount != null && totalAmount != null && 
            paidAmount.compareTo(totalAmount) > 0) {
            throw new IllegalArgumentException("Paid amount cannot exceed total amount");
        }
    }
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // Validate dates
        if (checkInDate != null && checkOutDate != null && 
            !checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }
    
    // ===== GETTERS AND SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getConfirmationNumber() { return confirmationNumber; }
    public void setConfirmationNumber(String confirmationNumber) { this.confirmationNumber = confirmationNumber; }
    
    public String getExternalConfirmationNumber() { return externalConfirmationNumber; }
    public void setExternalConfirmationNumber(String externalConfirmationNumber) { 
        this.externalConfirmationNumber = externalConfirmationNumber; 
    }
    
    public Guest getGuest() { return guest; }
    public void setGuest(Guest guest) { this.guest = guest; }
    
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    
    public Channel getChannel() { return channel; }
    public void setChannel(Channel channel) { this.channel = channel; }
    
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    
    public LocalDateTime getActualCheckInTime() { return actualCheckInTime; }
    public void setActualCheckInTime(LocalDateTime actualCheckInTime) { this.actualCheckInTime = actualCheckInTime; }
    
    public LocalDateTime getActualCheckOutTime() { return actualCheckOutTime; }
    public void setActualCheckOutTime(LocalDateTime actualCheckOutTime) { this.actualCheckOutTime = actualCheckOutTime; }
    
    public Integer getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(Integer numberOfGuests) { this.numberOfGuests = numberOfGuests; }
    
    public Integer getNumberOfChildren() { return numberOfChildren; }
    public void setNumberOfChildren(Integer numberOfChildren) { this.numberOfChildren = numberOfChildren; }
    
    public Integer getNumberOfInfants() { return numberOfInfants; }
    public void setNumberOfInfants(Integer numberOfInfants) { this.numberOfInfants = numberOfInfants; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    
    public GuestType getGuestType() { return guestType; }
    public void setGuestType(GuestType guestType) { this.guestType = guestType; }
    
    public Boolean getIsGroupBooking() { return isGroupBooking; }
    public void setIsGroupBooking(Boolean isGroupBooking) { this.isGroupBooking = isGroupBooking; }
    
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    
    public String getInternalNotes() { return internalNotes; }
    public void setInternalNotes(String internalNotes) { this.internalNotes = internalNotes; }
    
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
    
    public LocalDateTime getCancellationDate() { return cancellationDate; }
    public void setCancellationDate(LocalDateTime cancellationDate) { this.cancellationDate = cancellationDate; }
    
    public String getRatePlanCode() { return ratePlanCode; }
    public void setRatePlanCode(String ratePlanCode) { this.ratePlanCode = ratePlanCode; }
    
    public String getPromotionCode() { return promotionCode; }
    public void setPromotionCode(String promotionCode) { this.promotionCode = promotionCode; }
    
    public String getMarketSegment() { return marketSegment; }
    public void setMarketSegment(String marketSegment) { this.marketSegment = marketSegment; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
    
    public List<FolioCharge> getFolioCharges() { return folioCharges; }
    public void setFolioCharges(List<FolioCharge> folioCharges) { this.folioCharges = folioCharges; }
    
    // ===== EQUALS AND HASHCODE =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return confirmationNumber != null && confirmationNumber.equals(that.confirmationNumber);
    }
    
    @Override
    public int hashCode() {
        return confirmationNumber != null ? confirmationNumber.hashCode() : 0;
    }
    
    // ===== TO STRING =====
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", confirmationNumber='" + confirmationNumber + '\'' +
                ", guestName='" + (guest != null ? guest.getFullName() : "N/A") + '\'' +
                ", roomNumber='" + (room != null ? room.getRoomNumber() : "N/A") + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                '}';
    }
}

enum GuestType {
    INDIVIDUAL("Individual"),
    CORPORATE("Corporate"),
    GROUP("Group"),
    VIP("VIP"),
    GOVERNMENT("Government"),
    AIRLINE_CREW("Airline Crew");
    
    private final String displayName;
    
    GuestType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}