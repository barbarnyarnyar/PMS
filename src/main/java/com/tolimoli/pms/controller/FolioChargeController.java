package com.tolimoli.pms.controller;

import com.tolimoli.pms.dto.response.ApiResponse;
import com.tolimoli.pms.entity.ChargeType;
import com.tolimoli.pms.entity.FolioCharge;
import com.tolimoli.pms.service.FolioChargeService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Map;

/**
 * Folio Charge Controller - REST API for folio charge management
 */
@RestController
@RequestMapping("/api/folio-charges")
@CrossOrigin(origins = "*")
public class FolioChargeController {

    @Autowired
    private FolioChargeService folioChargeService;

    /**
     * Add a service charge to reservation
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FolioCharge>> addServiceCharge(
            @RequestParam Long reservationId,
            @RequestParam String description,
            @RequestParam BigDecimal amount,
            @RequestParam ChargeType chargeType) {

        FolioCharge charge = folioChargeService.addServiceCharge(reservationId, description, amount, chargeType);
        ApiResponse<FolioCharge> response = new ApiResponse<>("success", "Service charge added successfully", charge);
        return ResponseEntity.ok(response);
    }

    /**
     * Add a charge with quantity
     */
    @PostMapping("/with-quantity")
    public ResponseEntity<ApiResponse<FolioCharge>> addChargeWithQuantity(
            @RequestParam Long reservationId,
            @RequestParam String description,
            @RequestParam BigDecimal unitPrice,
            @RequestParam Integer quantity,
            @RequestParam ChargeType chargeType) {

        FolioCharge charge = folioChargeService.addChargeWithQuantity(reservationId, description, unitPrice, quantity,
                chargeType);
        ApiResponse<FolioCharge> response = new ApiResponse<>("success", "Charge with quantity added successfully",
                charge);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all charges for a reservation
     */
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ApiResponse<List<FolioCharge>>> getReservationCharges(@PathVariable Long reservationId) {
        List<FolioCharge> charges = folioChargeService.getReservationCharges(reservationId);
        ApiResponse<List<FolioCharge>> response = new ApiResponse<>("success", "Charges retrieved successfully",
                charges);
        return ResponseEntity.ok(response);
    }

    /**
     * Calculate total charges for a reservation
     */
    @GetMapping("/reservation/{reservationId}/total")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateTotalCharges(@PathVariable Long reservationId) {
        BigDecimal totalCharges = folioChargeService.calculateTotalCharges(reservationId);

        Map<String, Object> chargeInfo = Map.of(
                "reservationId", reservationId,
                "totalCharges", totalCharges);

        ApiResponse<Map<String, Object>> response = new ApiResponse<>("success",
                "Total charges calculated successfully", chargeInfo);
        return ResponseEntity.ok(response);
    }

    /**
     * Mark charges as paid for a reservation
     */
    @PutMapping("/reservation/{reservationId}/mark-paid")
    public ResponseEntity<ApiResponse<String>> markChargesAsPaid(@PathVariable Long reservationId) {
        folioChargeService.markChargesAsPaid(reservationId);
        ApiResponse<String> response = new ApiResponse<>("success", "Charges marked as paid successfully",
                "All charges marked as paid");
        return ResponseEntity.ok(response);
    }
}