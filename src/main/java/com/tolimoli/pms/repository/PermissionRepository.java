package com.tolimoli.pms.repository;

import com.tolimoli.pms.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByName(String name);
    
    List<Permission> findByActiveTrue();
    
    List<Permission> findByCategory(String category);
    
    List<Permission> findByCategoryAndActiveTrue(String category);
    
    Boolean existsByName(String name);
    
    @Query("SELECT p FROM Permission p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Permission> searchPermissions(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(p) FROM Permission p WHERE p.active = true")
    Long countActivePermissions();
    
    @Query("SELECT DISTINCT p.category FROM Permission p WHERE p.category IS NOT NULL AND p.active = true")
    List<String> findAllCategories();
}