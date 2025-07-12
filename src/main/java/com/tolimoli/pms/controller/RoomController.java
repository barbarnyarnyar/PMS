package com.tolimoli.pms.controller;

import com.tolimoli.pms.dto.response.ApiResponse;
import com.tolimoli.pms.entity.Room;
import com.tolimoli.pms.entity.RoomType;
import com.tolimoli.pms.entity.RoomStatus;
import com.tolimoli.pms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Room Controller - REST API for room management
 */
@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * Create a new room
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Room>> createRoom(
            @RequestParam String roomNumber,
            @RequestParam RoomType roomType,
            @RequestParam Integer capacity,
            @RequestParam BigDecimal baseRate) {
        
        Room room = roomService.createRoom(roomNumber, roomType, capacity, baseRate);
        ApiResponse<Room> response = new ApiResponse<>("success", "Room created successfully", room);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all rooms
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Room>>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        ApiResponse<List<Room>> response = new ApiResponse<>("success", "Rooms retrieved successfully", rooms);
        return ResponseEntity.ok(response);
    }

    /**
     * Get room by number
     */
    @GetMapping("/{roomNumber}")
    public ResponseEntity<ApiResponse<Room>> getRoomByNumber(@PathVariable String roomNumber) {
        return roomService.getRoomByNumber(roomNumber)
                .map(room -> {
                    ApiResponse<Room> response = new ApiResponse<>("success", "Room found", room);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get available rooms for date range and room type
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Room>>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) RoomType roomType) {
        
        List<Room> availableRooms = roomService.getAvailableRooms(checkIn, checkOut, roomType);
        ApiResponse<List<Room>> response = new ApiResponse<>("success", "Available rooms retrieved successfully", availableRooms);
        return ResponseEntity.ok(response);
    }

    /**
     * Update room status
     */
    @PutMapping("/{roomId}/status")
    public ResponseEntity<ApiResponse<Room>> updateRoomStatus(
            @PathVariable Long roomId,
            @RequestParam RoomStatus status) {
        
        Room room = roomService.updateRoomStatus(roomId, status);
        ApiResponse<Room> response = new ApiResponse<>("success", "Room status updated successfully", room);
        return ResponseEntity.ok(response);
    }
}