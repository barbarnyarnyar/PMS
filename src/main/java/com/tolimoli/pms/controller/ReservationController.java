package com.tolimoli.pms.controller;

import com.tolimoli.pms.dto.response.ApiResponse;
import com.tolimoli.pms.entity.Reservation;
import com.tolimoli.pms.entity.Room;
import com.tolimoli.pms.entity.RoomType;
import com.tolimoli.pms.service.ReservationService;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Reservation Controller - REST API for reservation management
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * Create a new reservation
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Reservation>> createReservation(
            @RequestParam String guestEmail,
            @RequestParam String roomNumber,
            @RequestParam String channelCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam Integer numberOfGuests,
            @RequestParam(required = false) String specialRequests) {
        
        Reservation reservation = reservationService.createReservation(
                guestEmail, roomNumber, channelCode, checkInDate, checkOutDate, numberOfGuests, specialRequests);
        
        ApiResponse<Reservation> response = new ApiResponse<>("success", "Reservation created successfully", reservation);
        return ResponseEntity.ok(response);
    }

    /**
     * Get reservation by confirmation number
     */
    @GetMapping("/{confirmationNumber}")
    public ResponseEntity<ApiResponse<Reservation>> getReservationByConfirmation(@PathVariable String confirmationNumber) {
        return reservationService.getReservationByConfirmation(confirmationNumber)
                .map(reservation -> {
                    ApiResponse<Reservation> response = new ApiResponse<>("success", "Reservation found", reservation);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Check-in guest
     */
    @PutMapping("/{reservationId}/checkin")
    public ResponseEntity<ApiResponse<Reservation>> checkInGuest(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.checkInGuest(reservationId);
        ApiResponse<Reservation> response = new ApiResponse<>("success", "Guest checked in successfully", reservation);
        return ResponseEntity.ok(response);
    }

    /**
     * Check-out guest
     */
    @PutMapping("/{reservationId}/checkout")
    public ResponseEntity<ApiResponse<Reservation>> checkOutGuest(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.checkOutGuest(reservationId);
        ApiResponse<Reservation> response = new ApiResponse<>("success", "Guest checked out successfully", reservation);
        return ResponseEntity.ok(response);
    }

    /**
     * Check room availability
     */
    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<List<Room>>> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) RoomType roomType) {
        
        List<Room> availableRooms = reservationService.checkAvailability(checkIn, checkOut, roomType);
        ApiResponse<List<Room>> response = new ApiResponse<>("success", "Availability checked successfully", availableRooms);
        return ResponseEntity.ok(response);
    }
}