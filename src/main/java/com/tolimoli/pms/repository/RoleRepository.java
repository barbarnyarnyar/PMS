package com.tolimoli.pms.repository;

import com.tolimoli.pms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(String name);
    
    List<Role> findByActiveTrue();
    
    Boolean existsByName(String name);
    
    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.name = :permissionName")
    List<Role> findRolesWithPermission(@Param("permissionName") String permissionName);
    
    @Query("SELECT r FROM Role r WHERE r.active = true AND " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Role> searchRoles(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(r) FROM Role r WHERE r.active = true")
    Long countActiveRoles();
}