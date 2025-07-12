package com.tolimoli.pms.controller;

import com.tolimoli.pms.dto.response.ApiResponse;
import com.tolimoli.pms.entity.Rate;
import com.tolimoli.pms.service.RateService;
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
 * Rate Controller - REST API for rate management
 */
@RestController
@RequestMapping("/api/rates")
@CrossOrigin(origins = "*")
public class RateController {

    @Autowired
    private RateService rateService;

    /**
     * Set/Update rate for a room and channel
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Rate>> setRate(
            @RequestParam Long roomId,
            @RequestParam Long channelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam BigDecimal rateAmount,
            @RequestParam Integer availableRooms) {
        
        Rate rate = rateService.setRate(roomId, channelId, date, rateAmount, availableRooms);
        ApiResponse<Rate> response = new ApiResponse<>("success", "Rate set successfully", rate);
        return ResponseEntity.ok(response);
    }

    /**
     * Get rates for a room within date range
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<Rate>>> getRatesForRoom(
            @PathVariable Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Rate> rates = rateService.getRatesForRoom(roomId, startDate, endDate);
        ApiResponse<List<Rate>> response = new ApiResponse<>("success", "Rates retrieved successfully", rates);
        return ResponseEntity.ok(response);
    }

    /**
     * Get rates for a channel on specific date
     */
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<ApiResponse<List<Rate>>> getRatesForChannel(
            @PathVariable Long channelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<Rate> rates = rateService.getRatesForChannel(channelId, date);
        ApiResponse<List<Rate>> response = new ApiResponse<>("success", "Channel rates retrieved successfully", rates);
        return ResponseEntity.ok(response);
    }

    /**
     * Bulk update rates for multiple dates
     */
    @PostMapping("/bulk-update")
    public ResponseEntity<ApiResponse<String>> bulkUpdateRates(
            @RequestParam Long roomId,
            @RequestParam Long channelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam BigDecimal rateAmount,
            @RequestParam Integer availability) {
        
        rateService.bulkUpdateRates(roomId, channelId, startDate, endDate, rateAmount, availability);
        
        String message = String.format("Rates updated from %s to %s for room %d and channel %d", 
                startDate, endDate, roomId, channelId);
        
        ApiResponse<String> response = new ApiResponse<>("success", "Bulk rates updated successfully", message);
        return ResponseEntity.ok(response);
    }

    /**
     * Block sales for specific room, channel and date
     */
    @PutMapping("/block-sales")
    public ResponseEntity<ApiResponse<String>> blockSales(
            @RequestParam Long roomId,
            @RequestParam Long channelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        rateService.blockSales(roomId, channelId, date);
        
        String message = String.format("Sales blocked for room %d, channel %d on %s", roomId, channelId, date);
        ApiResponse<String> response = new ApiResponse<>("success", "Sales blocked successfully", message);
        return ResponseEntity.ok(response);
    }
}