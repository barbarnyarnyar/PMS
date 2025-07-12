package com.tolimoli.pms.controller;

import com.tolimoli.pms.dto.response.ApiResponse;
import com.tolimoli.pms.entity.Guest;
import com.tolimoli.pms.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Guest Controller - REST API for guest management
 */
@RestController
@RequestMapping("/api/guests")
@CrossOrigin(origins = "*")
public class GuestController {

    @Autowired
    private GuestService guestService;

    /**
     * Find guest by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Guest>> getGuestByEmail(@PathVariable String email) {
        return guestService.findGuestByEmail(email)
                .map(guest -> {
                    ApiResponse<Guest> response = new ApiResponse<>("success", "Guest found", guest);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}