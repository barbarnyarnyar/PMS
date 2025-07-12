package com.tolimoli.pms.controller;

import com.tolimoli.pms.dto.response.ApiResponse;
import com.tolimoli.pms.entity.Channel;
import com.tolimoli.pms.service.ChannelService;
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

/**
 * Channel Controller - REST API for channel management
 */
@RestController
@RequestMapping("/api/channels")
@CrossOrigin(origins = "*")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    /**
     * Create a new channel
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Channel>> createChannel(
            @RequestParam String channelName,
            @RequestParam String channelCode,
            @RequestParam(required = false) BigDecimal commissionRate,
            @RequestParam(required = false) String apiEndpoint) {
        
        Channel channel = channelService.createChannel(channelName, channelCode, commissionRate, apiEndpoint);
        ApiResponse<Channel> response = new ApiResponse<>("success", "Channel created successfully", channel);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all active channels
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Channel>>> getAllActiveChannels() {
        List<Channel> channels = channelService.getAllActiveChannels();
        ApiResponse<List<Channel>> response = new ApiResponse<>("success", "Active channels retrieved successfully", channels);
        return ResponseEntity.ok(response);
    }

    /**
     * Get channel by code
     */
    @GetMapping("/{channelCode}")
    public ResponseEntity<ApiResponse<Channel>> getChannelByCode(@PathVariable String channelCode) {
        return channelService.getChannelByCode(channelCode)
                .map(channel -> {
                    ApiResponse<Channel> response = new ApiResponse<>("success", "Channel found", channel);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}