package com.tolimoli.pms.repository;

import com.tolimoli.pms.entity.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Channel Repository for managing booking channels
 * 
 * Handles data access operations for Channel entity including
 * CRUD operations, custom queries, and business-specific methods
 */
@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long>, 
                                         JpaSpecificationExecutor<Channel> {
    
    // ===== BASIC FINDER METHODS =====
    
    /**
     * Find channel by channel code (unique identifier)
     */
    Optional<Channel> findByChannelCode(String channelCode);
    
    /**
     * Find channel by channel name
     */
    Optional<Channel> findByChannelName(String channelName);
    
    /**
     * Check if channel code exists
     */
    boolean existsByChannelCode(String channelCode);
    
    /**
     * Check if channel name exists
     */
    boolean existsByChannelName(String channelName);
    
    // ===== STATUS-BASED QUERIES =====
    
    /**
     * Find all active channels
     */
    List<Channel> findByIsActiveTrue();
    
    /**
     * Find all inactive channels
     */
    List<Channel> findByIsActiveFalse();
    
    /**
     * Find active channels with pagination
     */
    Page<Channel> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find channels by active status
     */
    List<Channel> findByIsActive(Boolean isActive);
    
    // ===== COMMISSION-BASED QUERIES =====
    
    /**
     * Find channels with no commission (direct channels)
     */
    @Query("SELECT c FROM Channel c WHERE c.commissionRate = 0.0 OR c.commissionRate IS NULL")
    List<Channel> findDirectChannels();
    
    /**
     * Find OTA channels (with commission)
     */
    @Query("SELECT c FROM Channel c WHERE c.commissionRate > 0.0")
    List<Channel> findOTAChannels();
    
    /**
     * Find channels by commission rate range
     */
    @Query("SELECT c FROM Channel c WHERE c.commissionRate BETWEEN :minRate AND :maxRate")
    List<Channel> findByCommissionRateBetween(@Param("minRate") BigDecimal minRate, 
                                            @Param("maxRate") BigDecimal maxRate);
    
    /**
     * Find channels with commission rate greater than specified value
     */
    List<Channel> findByCommissionRateGreaterThan(BigDecimal commissionRate);
    
    /**
     * Find channels with commission rate less than specified value
     */
    List<Channel> findByCommissionRateLessThan(BigDecimal commissionRate);
    
    // ===== API INTEGRATION QUERIES =====
    
    /**
     * Find channels with API integration
     */
    @Query("SELECT c FROM Channel c WHERE c.apiEndpoint IS NOT NULL AND c.apiEndpoint != ''")
    List<Channel> findChannelsWithAPI();
    
    /**
     * Find channels without API integration
     */
    @Query("SELECT c FROM Channel c WHERE c.apiEndpoint IS NULL OR c.apiEndpoint = ''")
    List<Channel> findChannelsWithoutAPI();
    
    /**
     * Find online channels (active and has API)
     */
    @Query("SELECT c FROM Channel c WHERE c.isActive = true " +
           "AND c.apiEndpoint IS NOT NULL AND c.apiEndpoint != ''")
    List<Channel> findOnlineChannels();
    
    // ===== SEARCH QUERIES =====
    
    /**
     * Search channels by name or code (case-insensitive)
     */
    @Query("SELECT c FROM Channel c WHERE " +
           "LOWER(c.channelName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.channelCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Channel> searchChannels(@Param("searchTerm") String searchTerm);
    
    /**
     * Search active channels only
     */
    @Query("SELECT c FROM Channel c WHERE c.isActive = true AND " +
           "(LOWER(c.channelName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.channelCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Channel> searchActiveChannels(@Param("searchTerm") String searchTerm);
    
    /**
     * Advanced search with multiple criteria
     */
    @Query("SELECT c FROM Channel c WHERE " +
           "(:name IS NULL OR LOWER(c.channelName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:code IS NULL OR LOWER(c.channelCode) LIKE LOWER(CONCAT('%', :code, '%'))) " +
           "AND (:isActive IS NULL OR c.isActive = :isActive) " +
           "AND (:hasAPI IS NULL OR " +
           "     (:hasAPI = true AND c.apiEndpoint IS NOT NULL AND c.apiEndpoint != '') OR " +
           "     (:hasAPI = false AND (c.apiEndpoint IS NULL OR c.apiEndpoint = ''))) " +
           "ORDER BY c.channelName")
    Page<Channel> findWithFilters(@Param("name") String name,
                                 @Param("code") String code,
                                 @Param("isActive") Boolean isActive,
                                 @Param("hasAPI") Boolean hasAPI,
                                 Pageable pageable);
    
    // ===== PERFORMANCE QUERIES =====
    
    /**
     * Get channel performance metrics
     */
    @Query("SELECT c.channelName, c.channelCode, COUNT(r), SUM(r.totalAmount), " +
           "AVG(r.totalAmount), SUM(r.totalAmount * c.commissionRate / 100) " +
           "FROM Channel c LEFT JOIN c.reservations r " +
           "WHERE r.status != 'CANCELLED' " +
           "AND r.checkInDate >= :startDate AND r.checkInDate <= :endDate " +
           "GROUP BY c.id, c.channelName, c.channelCode " +
           "ORDER BY SUM(r.totalAmount) DESC")
    List<Object[]> getChannelPerformance(@Param("startDate") java.time.LocalDate startDate,
                                        @Param("endDate") java.time.LocalDate endDate);
    
    /**
     * Get top performing channels
     */
    @Query("SELECT c FROM Channel c LEFT JOIN c.reservations r " +
           "WHERE r.status != 'CANCELLED' " +
           "GROUP BY c.id " +
           "ORDER BY SUM(r.totalAmount) DESC")
    List<Channel> findTopPerformingChannels(Pageable pageable);
    
    /**
     * Get channel booking count
     */
    @Query("SELECT COUNT(r) FROM Channel c LEFT JOIN c.reservations r " +
           "WHERE c.id = :channelId AND r.status != 'CANCELLED'")
    long getChannelBookingCount(@Param("channelId") Long channelId);
    
    /**
     * Get channel revenue
     */
    @Query("SELECT COALESCE(SUM(r.totalAmount), 0) FROM Channel c LEFT JOIN c.reservations r " +
           "WHERE c.id = :channelId AND r.status != 'CANCELLED'")
    BigDecimal getChannelRevenue(@Param("channelId") Long channelId);
    
    /**
     * Get channel commission earned
     */
    @Query("SELECT COALESCE(SUM(r.totalAmount * c.commissionRate / 100), 0) " +
           "FROM Channel c LEFT JOIN c.reservations r " +
           "WHERE c.id = :channelId AND r.status != 'CANCELLED'")
    BigDecimal getChannelCommissionEarned(@Param("channelId") Long channelId);
    
    // ===== STATISTICAL QUERIES =====
    
    /**
     * Get channel count by status
     */
    @Query("SELECT c.isActive, COUNT(c) FROM Channel c GROUP BY c.isActive")
    List<Object[]> getChannelCountByStatus();
    
    /**
     * Get average commission rate
     */
    @Query("SELECT AVG(c.commissionRate) FROM Channel c WHERE c.commissionRate > 0")
    BigDecimal getAverageCommissionRate();
    
    /**
     * Get channels with above average commission
     */
    @Query("SELECT c FROM Channel c WHERE c.commissionRate > " +
           "(SELECT AVG(ch.commissionRate) FROM Channel ch WHERE ch.commissionRate > 0)")
    List<Channel> findChannelsWithAboveAverageCommission();
    
    /**
     * Get monthly channel activity
     */
    @Query("SELECT EXTRACT(YEAR FROM r.createdAt), EXTRACT(MONTH FROM r.createdAt), " +
           "c.channelName, COUNT(r) " +
           "FROM Channel c LEFT JOIN c.reservations r " +
           "WHERE r.createdAt >= :startDate " +
           "GROUP BY EXTRACT(YEAR FROM r.createdAt), EXTRACT(MONTH FROM r.createdAt), " +
           "c.id, c.channelName " +
           "ORDER BY EXTRACT(YEAR FROM r.createdAt), EXTRACT(MONTH FROM r.createdAt)")
    List<Object[]> getMonthlyChannelActivity(@Param("startDate") LocalDateTime startDate);
    
    // ===== UPDATE OPERATIONS =====
    
    /**
     * Update channel status
     */
    @Modifying
    @Query("UPDATE Channel c SET c.isActive = :isActive, c.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE c.id = :channelId")
    int updateChannelStatus(@Param("channelId") Long channelId, @Param("isActive") Boolean isActive);
    
    /**
     * Update commission rate
     */
    @Modifying
    @Query("UPDATE Channel c SET c.commissionRate = :commissionRate, c.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE c.id = :channelId")
    int updateCommissionRate(@Param("channelId") Long channelId, 
                           @Param("commissionRate") BigDecimal commissionRate);
    
    /**
     * Update API endpoint
     */
    @Modifying
    @Query("UPDATE Channel c SET c.apiEndpoint = :apiEndpoint, c.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE c.id = :channelId")
    int updateApiEndpoint(@Param("channelId") Long channelId, 
                         @Param("apiEndpoint") String apiEndpoint);
    
    /**
     * Bulk activate channels
     */
    @Modifying
    @Query("UPDATE Channel c SET c.isActive = true, c.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE c.id IN :channelIds")
    int activateChannels(@Param("channelIds") List<Long> channelIds);
    
    /**
     * Bulk deactivate channels
     */
    @Modifying
    @Query("UPDATE Channel c SET c.isActive = false, c.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE c.id IN :channelIds")
    int deactivateChannels(@Param("channelIds") List<Long> channelIds);
    
    // ===== VALIDATION QUERIES =====
    
    /**
     * Find channels with duplicate names
     */
    @Query("SELECT c.channelName, COUNT(c) FROM Channel c " +
           "GROUP BY c.channelName HAVING COUNT(c) > 1")
    List<Object[]> findDuplicateChannelNames();
    
    /**
     * Find channels with invalid commission rates
     */
    @Query("SELECT c FROM Channel c WHERE c.commissionRate < 0 OR c.commissionRate > 100")
    List<Channel> findChannelsWithInvalidCommission();
    
    /**
     * Find channels without recent activity
     */
    @Query("SELECT c FROM Channel c WHERE c.id NOT IN " +
           "(SELECT DISTINCT r.channel.id FROM Reservation r " +
           "WHERE r.createdAt >= :cutoffDate)")
    List<Channel> findInactiveChannels(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // ===== CUSTOM FINDER METHODS =====
    
    /**
     * Find channels by name containing (case-insensitive)
     */
    List<Channel> findByChannelNameContainingIgnoreCase(String name);
    
    /**
     * Find channels by code containing (case-insensitive)
     */
    List<Channel> findByChannelCodeContainingIgnoreCase(String code);
    
    /**
     * Find channels created within date range
     */
    List<Channel> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find channels updated within date range
     */
    List<Channel> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find recently created channels
     */
    default List<Channel> findRecentlyCreated(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return findByCreatedAtBetween(cutoffDate, LocalDateTime.now());
    }
    
    /**
     * Find recently updated channels
     */
    default List<Channel> findRecentlyUpdated(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return findByUpdatedAtBetween(cutoffDate, LocalDateTime.now());
    }
    
    /**
     * Check if channel is direct booking
     */
    default boolean isDirectChannel(String channelCode) {
        return "DIRECT".equalsIgnoreCase(channelCode);
    }
    
    /**
     * Find all OTA channels (non-direct)
     */
    default List<Channel> findAllOTAChannels() {
        return findOTAChannels();
    }
}