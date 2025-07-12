package com.tolimoli.pms.config;

import com.tolimoli.pms.entity.Permission;
import com.tolimoli.pms.entity.Role;
import com.tolimoli.pms.entity.User;
import com.tolimoli.pms.repository.PermissionRepository;
import com.tolimoli.pms.repository.RoleRepository;
import com.tolimoli.pms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializePermissions();
        initializeRoles();
        initializeUsers();
    }

    private void initializePermissions() {
        String[][] permissions = {
            // User Management
            {"USER_MANAGEMENT", "Complete user management access", "USER_MANAGEMENT"},
            {"USER_VIEW", "View user information", "USER_MANAGEMENT"},
            {"USER_CREATE", "Create new users", "USER_MANAGEMENT"},
            {"USER_EDIT", "Edit user information", "USER_MANAGEMENT"},
            {"USER_DELETE", "Delete users", "USER_MANAGEMENT"},
            {"USER_PASSWORD_RESET", "Reset user passwords", "USER_MANAGEMENT"},
            
            // Role Management
            {"ROLE_MANAGEMENT", "Complete role management access", "ROLE_MANAGEMENT"},
            {"ROLE_VIEW", "View roles", "ROLE_MANAGEMENT"},
            {"ROLE_CREATE", "Create new roles", "ROLE_MANAGEMENT"},
            {"ROLE_EDIT", "Edit roles", "ROLE_MANAGEMENT"},
            {"ROLE_DELETE", "Delete roles", "ROLE_MANAGEMENT"},
            
            // Permission Management
            {"PERMISSION_MANAGEMENT", "Complete permission management access", "PERMISSION_MANAGEMENT"},
            {"PERMISSION_VIEW", "View permissions", "PERMISSION_MANAGEMENT"},
            
            // Room Management
            {"ROOM_MANAGEMENT", "Complete room management access", "ROOM"},
            {"ROOM_VIEW", "View room information", "ROOM"},
            {"ROOM_CREATE", "Create new rooms", "ROOM"},
            {"ROOM_EDIT", "Edit room information", "ROOM"},
            {"ROOM_DELETE", "Delete rooms", "ROOM"},
            {"ROOM_STATUS_CHANGE", "Change room status", "ROOM"},
            
            // Reservation Management
            {"RESERVATION_MANAGEMENT", "Complete reservation management access", "RESERVATION"},
            {"RESERVATION_VIEW", "View reservations", "RESERVATION"},
            {"RESERVATION_CREATE", "Create new reservations", "RESERVATION"},
            {"RESERVATION_EDIT", "Edit reservations", "RESERVATION"},
            {"RESERVATION_DELETE", "Delete reservations", "RESERVATION"},
            {"RESERVATION_CHECKIN", "Check-in guests", "RESERVATION"},
            {"RESERVATION_CHECKOUT", "Check-out guests", "RESERVATION"},
            
            // Payment Management
            {"PAYMENT_MANAGEMENT", "Complete payment management access", "PAYMENT"},
            {"PAYMENT_VIEW", "View payments", "PAYMENT"},
            {"PAYMENT_PROCESS", "Process payments", "PAYMENT"},
            {"PAYMENT_REFUND", "Process refunds", "PAYMENT"},
            
            // Channel Management
            {"CHANNEL_MANAGEMENT", "Complete channel management access", "CHANNEL"},
            {"CHANNEL_VIEW", "View channels", "CHANNEL"},
            {"CHANNEL_CREATE", "Create new channels", "CHANNEL"},
            {"CHANNEL_EDIT", "Edit channels", "CHANNEL"},
            
            // Rate Management
            {"RATE_MANAGEMENT", "Complete rate management access", "RATE"},
            {"RATE_VIEW", "View rates", "RATE"},
            {"RATE_CREATE", "Create new rates", "RATE"},
            {"RATE_EDIT", "Edit rates", "RATE"},
            
            // Folio Management
            {"FOLIO_MANAGEMENT", "Complete folio management access", "FOLIO"},
            {"FOLIO_VIEW", "View folio charges", "FOLIO"},
            {"FOLIO_CREATE", "Create folio charges", "FOLIO"},
            {"FOLIO_EDIT", "Edit folio charges", "FOLIO"},
            
            // Guest Management
            {"GUEST_MANAGEMENT", "Complete guest management access", "GUEST"},
            {"GUEST_VIEW", "View guest information", "GUEST"},
            {"GUEST_CREATE", "Create guest profiles", "GUEST"},
            {"GUEST_EDIT", "Edit guest information", "GUEST"}
        };

        for (String[] permData : permissions) {
            if (!permissionRepository.existsByName(permData[0])) {
                Permission permission = new Permission(permData[0], permData[1], permData[2]);
                permissionRepository.save(permission);
            }
        }
    }

    private void initializeRoles() {
        // Admin Role - Full access
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role("ROLE_ADMIN", "Hotel Administrator - Full system access");
            permissionRepository.findAll().forEach(permission -> {
                adminRole.getPermissions().add(permission);
                permission.getRoles().add(adminRole);
            });
            roleRepository.save(adminRole);
        }

        // Manager Role - Management access
        if (!roleRepository.existsByName("ROLE_MANAGER")) {
            Role managerRole = new Role("ROLE_MANAGER", "Hotel Manager - Management access");
            String[] managerPermissions = {
                "ROOM_MANAGEMENT", "RESERVATION_MANAGEMENT", "PAYMENT_MANAGEMENT",
                "CHANNEL_MANAGEMENT", "RATE_MANAGEMENT", "FOLIO_MANAGEMENT",
                "GUEST_MANAGEMENT", "USER_VIEW", "ROLE_VIEW"
            };
            addPermissionsToRole(managerRole, managerPermissions);
            roleRepository.save(managerRole);
        }

        // Receptionist Role - Front desk operations
        if (!roleRepository.existsByName("ROLE_RECEPTIONIST")) {
            Role receptionistRole = new Role("ROLE_RECEPTIONIST", "Receptionist - Front desk operations");
            String[] receptionistPermissions = {
                "ROOM_VIEW", "ROOM_STATUS_CHANGE", "RESERVATION_MANAGEMENT",
                "PAYMENT_VIEW", "PAYMENT_PROCESS", "GUEST_MANAGEMENT",
                "FOLIO_VIEW", "FOLIO_CREATE"
            };
            addPermissionsToRole(receptionistRole, receptionistPermissions);
            roleRepository.save(receptionistRole);
        }

        // User Role - Basic access
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role userRole = new Role("ROLE_USER", "Basic User - Limited access");
            String[] userPermissions = {
                "ROOM_VIEW", "RESERVATION_VIEW", "GUEST_VIEW"
            };
            addPermissionsToRole(userRole, userPermissions);
            roleRepository.save(userRole);
        }
    }

    private void addPermissionsToRole(Role role, String[] permissionNames) {
        for (String permissionName : permissionNames) {
            permissionRepository.findByName(permissionName)
                .ifPresent(permission -> {
                    role.getPermissions().add(permission);
                    permission.getRoles().add(role);
                });
        }
    }

    private void initializeUsers() {
        // Create default admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@tolimoli.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setDepartment("IT");

            roleRepository.findByName("ROLE_ADMIN")
                .ifPresent(role -> {
                    admin.getRoles().add(role);
                    role.getUsers().add(admin);
                });

            userRepository.save(admin);
        }

        // Create default manager user
        if (!userRepository.existsByUsername("manager")) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setEmail("manager@tolimoli.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setFirstName("Hotel");
            manager.setLastName("Manager");
            manager.setDepartment("Management");

            roleRepository.findByName("ROLE_MANAGER")
                .ifPresent(role -> {
                    manager.getRoles().add(role);
                    role.getUsers().add(manager);
                });

            userRepository.save(manager);
        }

        // Create default receptionist user
        if (!userRepository.existsByUsername("receptionist")) {
            User receptionist = new User();
            receptionist.setUsername("receptionist");
            receptionist.setEmail("reception@tolimoli.com");
            receptionist.setPassword(passwordEncoder.encode("reception123"));
            receptionist.setFirstName("Front");
            receptionist.setLastName("Desk");
            receptionist.setDepartment("Reception");

            roleRepository.findByName("ROLE_RECEPTIONIST")
                .ifPresent(role -> {
                    receptionist.getRoles().add(role);
                    role.getUsers().add(receptionist);
                });

            userRepository.save(receptionist);
        }
    }
}