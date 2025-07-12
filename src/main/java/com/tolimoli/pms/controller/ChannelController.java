package com.tolimoli.pms.controller;

import com.tolimoli.pms.dto.request.ChannelRequest;
import com.tolimoli.pms.dto.response.ChannelResponse;
import com.tolimoli.pms.dto.response.ChannelPerformanceResponse;
import com.tolimoli.pms.dto.response.ApiResponse;
import com.tolimoli.pms.entity.Channel;
import com.tolimoli.pms.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Channel Controller - REST API for channel management
 * 
 * Provides endpoints for managing booking channels including
 * CRUD operations, search, performance analytics, and reporting
 */
@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel Management", description = "APIs for managing booking channels and OTA integrations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChannelController {
    
    @Autowired
    private ChannelService channelService;
    
    // // ===== CRUD OPERATIONS =====
  
    // /**
    //  * Get channel statistics
    //  */
    // @GetMapping("/{id}/statistics")
    // @Operation(summary = "Get channel statistics", description = "Get detailed statistics for a specific channel")
    // public ResponseEntity<ApiResponse<ChannelPerformanceResponse>> getChannelStatistics(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id,
            
    //         @Parameter(description = "Start date for analysis", example = "2024-01-01")
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
    //         @Parameter(description = "End date for analysis", example = "2024-12-31")
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
    //     try {
    //         // Default to last 12 months if dates not provided
    //         if (startDate == null) startDate = LocalDate.now().minusMonths(12);
    //         if (endDate == null) endDate = LocalDate.now();
            
    //         ChannelPerformanceResponse statistics = channelService.getChannelStatistics(id, startDate, endDate);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(statistics, "Channel statistics retrieved successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel statistics failed: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Get top performing channels
    //  */
    // @GetMapping("/top-performers")
    // @Operation(summary = "Get top performing channels", description = "Get channels ranked by performance")
    // public ResponseEntity<ApiResponse<List<ChannelPerformanceResponse>>> getTopPerformingChannels(
    //         @Parameter(description = "Number of channels to return", example = "5")
    //         @RequestParam(defaultValue = "5") int limit,
            
    //         @Parameter(description = "Start date for analysis", example = "2024-01-01")
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
    //         @Parameter(description = "End date for analysis", example = "2024-12-31")
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
    //     try {
    //         if (startDate == null) startDate = LocalDate.now().minusMonths(12);
    //         if (endDate == null) endDate = LocalDate.now();
            
    //         List<ChannelPerformanceResponse> topChannels = channelService.getTopPerformingChannels(limit, startDate, endDate);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(topChannels, "Top performing channels retrieved successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Failed to retrieve top performing channels: " + e.getMessage())
    //         );
    //     }
    // }
    
    // // ===== RATE MANAGEMENT =====
    
    // /**
    //  * Get channel rates
    //  */
    // @GetMapping("/{id}/rates")
    // @Operation(summary = "Get channel rates", description = "Get rate information for a specific channel")
    // public ResponseEntity<ApiResponse<Object>> getChannelRates(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id,
            
    //         @Parameter(description = "Date for rates", example = "2024-03-15")
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            
    //         @Parameter(description = "Room ID filter")
    //         @RequestParam(required = false) Long roomId) {
        
    //     try {
    //         if (date == null) date = LocalDate.now();
            
    //         Object rates = channelService.getChannelRates(id, date, roomId);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(rates, "Channel rates retrieved successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel rates retrieval failed: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Sync rates to channel
    //  */
    // @PostMapping("/{id}/sync-rates")
    // @Operation(summary = "Sync rates to channel", description = "Synchronize rates and availability to the channel")
    // public ResponseEntity<ApiResponse<String>> syncRatesToChannel(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id,
            
    //         @Parameter(description = "Start date for sync", example = "2024-03-15")
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
    //         @Parameter(description = "End date for sync", example = "2024-03-16")
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
    //     try {
    //         String result = channelService.syncRatesToChannel(id, startDate, endDate);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(result, "Rates synchronized successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Rate synchronization failed: " + e.getMessage())
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Sync error: " + e.getMessage())
    //         );
    //     }
    // }
    
    // // ===== BULK OPERATIONS =====
    
    // /**
    //  * Bulk activate channels
    //  */
    // @PatchMapping("/bulk/activate")
    // @Operation(summary = "Bulk activate channels", description = "Activate multiple channels at once")
    // public ResponseEntity<ApiResponse<String>> bulkActivateChannels(
    //         @Parameter(description = "List of channel IDs", required = true)
    //         @RequestBody List<Long> channelIds) {
        
    //     try {
    //         int activatedCount = channelService.bulkActivateChannels(channelIds);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(
    //                 String.valueOf(activatedCount), 
    //                 activatedCount + " channels activated successfully"
    //             )
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Bulk activation failed: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Bulk deactivate channels
    //  */
    // @PatchMapping("/bulk/deactivate")
    // @Operation(summary = "Bulk deactivate channels", description = "Deactivate multiple channels at once")
    // public ResponseEntity<ApiResponse<String>> bulkDeactivateChannels(
    //         @Parameter(description = "List of channel IDs", required = true)
    //         @RequestBody List<Long> channelIds) {
        
    //     try {
    //         int deactivatedCount = channelService.bulkDeactivateChannels(channelIds);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(
    //                 String.valueOf(deactivatedCount), 
    //                 deactivatedCount + " channels deactivated successfully"
    //             )
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Bulk deactivation failed: " + e.getMessage())
    //         );
    //     }
    // }
    
    // // ===== VALIDATION AND TESTING =====
    
    // /**
    //  * Test channel connection
    //  */
    // @PostMapping("/{id}/test-connection")
    // @Operation(summary = "Test channel connection", description = "Test API connection to the channel")
    // public ResponseEntity<ApiResponse<String>> testChannelConnection(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id) {
        
    //     try {
    //         String result = channelService.testChannelConnection(id);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(result, "Channel connection test completed")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Connection test failed: " + e.getMessage())
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Connection error: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Validate channel configuration
    //  */
    // @PostMapping("/{id}/validate")
    // @Operation(summary = "Validate channel", description = "Validate channel configuration and settings")
    // public ResponseEntity<ApiResponse<String>> validateChannel(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id) {
        
    //     try {
    //         String result = channelService.validateChannel(id);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(result, "Channel validation completed")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel validation failed: " + e.getMessage())
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Validation error: " + e.getMessage())
    //         );
    //     }
    // }
    
    // // ===== REPORTING =====
    
    // /**
    //  * Get channel summary report
    //  */
    // @GetMapping("/report/summary")
    // @Operation(summary = "Get channel summary report", description = "Get overall channel performance summary")
    // public ResponseEntity<ApiResponse<Object>> getChannelSummaryReport(
    //         @Parameter(description = "Start date for report", example = "2024-01-01")
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
    //         @Parameter(description = "End date for report", example = "2024-12-31")
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
    //     try {
    //         if (startDate == null) startDate = LocalDate.now().minusMonths(12);
    //         if (endDate == null) endDate = LocalDate.now();
            
    //         Object report = channelService.getChannelSummaryReport(startDate, endDate);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(report, "Channel summary report generated successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Report generation failed: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Export channels to CSV
    //  */
    // @GetMapping("/export/csv")
    // @Operation(summary = "Export channels to CSV", description = "Export channel data to CSV format")
    // public ResponseEntity<byte[]> exportChannelsToCSV(
    //         @Parameter(description = "Include only active channels")
    //         @RequestParam(defaultValue = "false") boolean activeOnly) {
        
    //     try {
    //         byte[] csvData = channelService.exportChannelsToCSV(activeOnly);
            
    //         return ResponseEntity.ok()
    //                 .header("Content-Type", "text/csv")
    //                 .header("Content-Disposition", "attachment; filename=channels.csv")
    //                 .body(csvData);
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().build();
    //     }
    // }
    
    // // ===== UTILITY ENDPOINTS =====
    
    // /**
    //  * Get channel types
    //  */
    // @GetMapping("/types")
    // @Operation(summary = "Get channel types", description = "Get available channel types and categories")
    // public ResponseEntity<ApiResponse<Object>> getChannelTypes() {
        
    //     try {
    //         Object channelTypes = channelService.getChannelTypes();
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channelTypes, "Channel types retrieved successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Failed to retrieve channel types: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Get commission rate statistics
    //  */
    // @GetMapping("/commission-stats")
    // @Operation(summary = "Get commission statistics", description = "Get commission rate statistics across all channels")
    // public ResponseEntity<ApiResponse<Object>> getCommissionStatistics() {
        
    //     try {
    //         Object stats = channelService.getCommissionStatistics();
    //         return ResponseEntity.ok(
    //             ApiResponse.success(stats, "Commission statistics retrieved successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Failed to retrieve commission statistics: " + e.getMessage())
    //         );
    //     }
    // }

    // @ApiResponses(value = {
    //     @SwaggerApiResponse(responseCode = "200", description = "Channels retrieved successfully"),
    //     @SwaggerApiResponse(responseCode = "400", description = "Invalid request parameters")
    // })
    // public ResponseEntity<ApiResponse<Page<ChannelResponse>>> getAllChannels(
    //         @Parameter(description = "Page number (0-based)", example = "0")
    //         @RequestParam(defaultValue = "0") int page,
            
    //         @Parameter(description = "Page size", example = "10")
    //         @RequestParam(defaultValue = "10") int size,
            
    //         @Parameter(description = "Sort field", example = "channelName")
    //         @RequestParam(defaultValue = "channelName") String sortBy,
            
    //         @Parameter(description = "Sort direction", example = "ASC")
    //         @RequestParam(defaultValue = "ASC") String sortDir,
            
    //         @Parameter(description = "Filter by active status")
    //         @RequestParam(required = false) Boolean isActive) {
        
    //     try {
    //         Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
    //         Pageable pageable = PageRequest.of(page, size, sort);
            
    //         Page<ChannelResponse> channels = channelService.getAllChannels(pageable, isActive);
            
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channels, "Channels retrieved successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Failed to retrieve channels: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Get channel by ID
    //  */
    // @GetMapping("/{id}")
    // @Operation(summary = "Get channel by ID", description = "Retrieve a specific channel by its ID")
    // @ApiResponses(value = {
    //     @SwaggerApiResponse(responseCode = "200", description = "Channel found"),
    //     @SwaggerApiResponse(responseCode = "404", description = "Channel not found")
    // })
    // public ResponseEntity<ApiResponse<ChannelResponse>> getChannelById(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id) {
        
    //     try {
    //         ChannelResponse channel = channelService.getChannelById(id);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channel, "Channel retrieved successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel not found: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Get channel by code
    //  */
    // @GetMapping("/code/{channelCode}")
    // @Operation(summary = "Get channel by code", description = "Retrieve a channel by its unique code")
    // public ResponseEntity<ApiResponse<ChannelResponse>> getChannelByCode(
    //         @Parameter(description = "Channel code", required = true, example = "BOOKING_COM")
    //         @PathVariable String channelCode) {
        
    //     try {
    //         ChannelResponse channel = channelService.getChannelByCode(channelCode);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channel, "Channel retrieved successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel not found: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Create new channel
    //  */
    // @PostMapping
    // @Operation(summary = "Create new channel", description = "Create a new booking channel")
    // @ApiResponses(value = {
    //     @SwaggerApiResponse(responseCode = "201", description = "Channel created successfully"),
    //     @SwaggerApiResponse(responseCode = "400", description = "Invalid channel data"),
    //     @SwaggerApiResponse(responseCode = "409", description = "Channel code or name already exists")
    // })
    // public ResponseEntity<ApiResponse<ChannelResponse>> createChannel(
    //         @Parameter(description = "Channel data", required = true)
    //         @Valid @RequestBody ChannelRequest channelRequest) {
        
    //     try {
    //         ChannelResponse channel = channelService.createChannel(channelRequest);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(
    //             ApiResponse.success(channel, "Channel created successfully")
    //         );
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.status(HttpStatus.CONFLICT).body(
    //             ApiResponse.error("Channel creation failed: " + e.getMessage())
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Invalid channel data: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Update channel
    //  */
    // @PutMapping("/{id}")
    // @Operation(summary = "Update channel", description = "Update an existing channel")
    // @ApiResponses(value = {
    //     @SwaggerApiResponse(responseCode = "200", description = "Channel updated successfully"),
    //     @SwaggerApiResponse(responseCode = "404", description = "Channel not found"),
    //     @SwaggerApiResponse(responseCode = "400", description = "Invalid channel data")
    // })
    // public ResponseEntity<ApiResponse<ChannelResponse>> updateChannel(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id,
            
    //         @Parameter(description = "Updated channel data", required = true)
    //         @Valid @RequestBody ChannelRequest channelRequest) {
        
    //     try {
    //         ChannelResponse channel = channelService.updateChannel(id, channelRequest);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channel, "Channel updated successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel update failed: " + e.getMessage())
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Invalid channel data: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Delete channel
    //  */
    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete channel", description = "Delete a channel (soft delete)")
    // @ApiResponses(value = {
    //     @SwaggerApiResponse(responseCode = "200", description = "Channel deleted successfully"),
    //     @SwaggerApiResponse(responseCode = "404", description = "Channel not found"),
    //     @SwaggerApiResponse(responseCode = "409", description = "Channel has active bookings")
    // })
    // public ResponseEntity<ApiResponse<Void>> deleteChannel(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id) {
        
    //     try {
    //         channelService.deleteChannel(id);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(null, "Channel deleted successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel deletion failed: " + e.getMessage())
    //         );
    //     } catch (IllegalStateException e) {
    //         return ResponseEntity.status(HttpStatus.CONFLICT).body(
    //             ApiResponse.error("Cannot delete channel: " + e.getMessage())
    //         );
    //     }
    // }
    
    // // ===== CHANNEL STATUS OPERATIONS =====
    
    // /**
    //  * Activate channel
    //  */
    // @PatchMapping("/{id}/activate")
    // @Operation(summary = "Activate channel", description = "Activate a channel for bookings")
    // public ResponseEntity<ApiResponse<ChannelResponse>> activateChannel(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id) {
        
    //     try {
    //         ChannelResponse channel = channelService.activateChannel(id);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channel, "Channel activated successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel activation failed: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Deactivate channel
    //  */
    // @PatchMapping("/{id}/deactivate")
    // @Operation(summary = "Deactivate channel", description = "Deactivate a channel to stop bookings")
    // public ResponseEntity<ApiResponse<ChannelResponse>> deactivateChannel(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id) {
        
    //     try {
    //         ChannelResponse channel = channelService.deactivateChannel(id);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channel, "Channel deactivated successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Channel deactivation failed: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Update commission rate
    //  */
    // @PatchMapping("/{id}/commission")
    // @Operation(summary = "Update commission rate", description = "Update channel commission rate")
    // public ResponseEntity<ApiResponse<ChannelResponse>> updateCommissionRate(
    //         @Parameter(description = "Channel ID", required = true)
    //         @PathVariable Long id,
            
    //         @Parameter(description = "New commission rate", required = true, example = "15.0")
    //         @RequestParam BigDecimal commissionRate) {
        
    //     try {
    //         ChannelResponse channel = channelService.updateCommissionRate(id, commissionRate);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channel, "Commission rate updated successfully")
    //         );
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //             ApiResponse.error("Commission rate update failed: " + e.getMessage())
    //         );
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Invalid commission rate: " + e.getMessage())
    //         );
    //     }
    // }
    
    // // ===== SEARCH AND FILTER OPERATIONS =====
    
    // /**
    //  * Search channels
    //  */
    // @GetMapping("/search")
    // @Operation(summary = "Search channels", description = "Search channels by name or code")
    // public ResponseEntity<ApiResponse<List<ChannelResponse>>> searchChannels(
    //         @Parameter(description = "Search term", required = true, example = "booking")
    //         @RequestParam String searchTerm,
            
    //         @Parameter(description = "Include only active channels")
    //         @RequestParam(defaultValue = "true") boolean activeOnly) {
        
    //     try {
    //         List<ChannelResponse> channels = channelService.searchChannels(searchTerm, activeOnly);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channels, "Search completed successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Search failed: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Get active channels
    //  */
    // @GetMapping("/active")
    // @Operation(summary = "Get active channels", description = "Retrieve all active booking channels")
    // public ResponseEntity<ApiResponse<List<ChannelResponse>>> getActiveChannels() {
        
    //     try {
    //         List<ChannelResponse> channels = channelService.getActiveChannels();
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channels, "Active channels retrieved successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Failed to retrieve active channels: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Get OTA channels
    //  */
    // @GetMapping("/ota")
    // @Operation(summary = "Get OTA channels", description = "Retrieve all Online Travel Agency channels")
    // public ResponseEntity<ApiResponse<List<ChannelResponse>>> getOTAChannels() {
        
    //     try {
    //         List<ChannelResponse> channels = channelService.getOTAChannels();
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channels, "OTA channels retrieved successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Failed to retrieve OTA channels: " + e.getMessage())
    //         );
    //     }
    // }
    
    // /**
    //  * Get direct channels
    //  */
    // @GetMapping("/direct")
    // @Operation(summary = "Get direct channels", description = "Retrieve all direct booking channels")
    // public ResponseEntity<ApiResponse<List<ChannelResponse>>> getDirectChannels() {
        
    //     try {
    //         List<ChannelResponse> channels = channelService.getDirectChannels();
    //         return ResponseEntity.ok(
    //             ApiResponse.success(channels, "Direct channels retrieved successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Failed to retrieve direct channels: " + e.getMessage())
    //         );
    //     }
    // }
    
    // // ===== PERFORMANCE AND ANALYTICS =====
    
    // /**
    //  * Get channel performance
    //  */
    // @GetMapping("/performance")
    // @Operation(summary = "Get channel performance", description = "Retrieve performance metrics for all channels")
    // public ResponseEntity<ApiResponse<List<ChannelPerformanceResponse>>> getChannelPerformance(
    //         @Parameter(description = "Start date for analysis", example = "2024-01-01")
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
    //         @Parameter(description = "End date for analysis", example = "2024-12-31")
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
    //     try {
    //         List<ChannelPerformanceResponse> performance = channelService.getChannelPerformance(startDate, endDate);
    //         return ResponseEntity.ok(
    //             ApiResponse.success(performance, "Channel performance retrieved successfully")
    //         );
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(
    //             ApiResponse.error("Failed to retrieve channel performance: " + e.getMessage())
    //         );
    //     }
    //     return ResponseEntity.ok(
    //         ApiResponse.success(channels, "Channels retrieved successfully")
    //     );  
    // }
    // 
    // /**
    //  * Get channel statistics
    //  */
    // @GetMapping("/{id}/statistics")
    // @Operation(summary = "Get channel statistics", description = "Get detailed statistics for a specific channel")
    // public ResponseEntity<ApiResponse<ChannelPerformanceResponse>> getChannelStatistics(
    //         @Parameter(description = "Channel ID", required = true)
  }