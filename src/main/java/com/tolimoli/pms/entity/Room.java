package com.tolimoli.pms.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    
    private Integer capacity;
    private BigDecimal baseRate;
    
    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.AVAILABLE;
    
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
    
    // Constructors, getters, setters...
    public Room() {}
    
    public Room(String roomNumber, RoomType roomType, Integer capacity, BigDecimal baseRate) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.baseRate = baseRate;
    }
    
    // Business methods
    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE && isActive;
    }
    
    public void markAsOccupied() {
        this.status = RoomStatus.OCCUPIED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsAvailable() {
        this.status = RoomStatus.AVAILABLE;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public BigDecimal getBaseRate() { return baseRate; }
    public void setBaseRate(BigDecimal baseRate) { this.baseRate = baseRate; }
    
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}