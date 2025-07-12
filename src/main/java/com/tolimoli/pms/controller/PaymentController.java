package com.tolimoli.pms.controller;

import com.tolimoli.pms.dto.response.ApiResponse;
import com.tolimoli.pms.entity.Payment;
import com.tolimoli.pms.entity.PaymentMethod;
import com.tolimoli.pms.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Payment Controller - REST API for payment management
 */
@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Process a payment
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> processPayment(
            @RequestParam Long reservationId,
            @RequestParam BigDecimal amount,
            @RequestParam PaymentMethod paymentMethod,
            @RequestParam(required = false) String transactionId) {
        
        Payment payment = paymentService.processPayment(reservationId, amount, paymentMethod, transactionId);
        ApiResponse<Payment> response = new ApiResponse<>("success", "Payment processed successfully", payment);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all payments for a reservation
     */
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ApiResponse<List<Payment>>> getReservationPayments(@PathVariable Long reservationId) {
        List<Payment> payments = paymentService.getReservationPayments(reservationId);
        ApiResponse<List<Payment>> response = new ApiResponse<>("success", "Payments retrieved successfully", payments);
        return ResponseEntity.ok(response);
    }

    /**
     * Get outstanding balance for a reservation
     */
    @GetMapping("/reservation/{reservationId}/balance")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOutstandingBalance(@PathVariable Long reservationId) {
        BigDecimal totalPayments = paymentService.calculateTotalPayments(reservationId);
        BigDecimal outstandingBalance = paymentService.getOutstandingBalance(reservationId);
        
        Map<String, Object> balanceInfo = Map.of(
                "reservationId", reservationId,
                "totalPayments", totalPayments,
                "outstandingBalance", outstandingBalance,
                "isFullyPaid", outstandingBalance.compareTo(BigDecimal.ZERO) <= 0
        );
        
        ApiResponse<Map<String, Object>> response = new ApiResponse<>("success", "Balance information retrieved successfully", balanceInfo);
        return ResponseEntity.ok(response);
    }

    /**
     * Process a refund
     */
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<ApiResponse<Payment>> processRefund(
            @PathVariable Long paymentId,
            @RequestParam BigDecimal refundAmount) {
        
        Payment refund = paymentService.processRefund(paymentId, refundAmount);
        ApiResponse<Payment> response = new ApiResponse<>("success", "Refund processed successfully", refund);
        return ResponseEntity.ok(response);
    }
}