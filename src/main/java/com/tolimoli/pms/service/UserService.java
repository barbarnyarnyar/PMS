package com.tolimoli.pms.service;

import com.tolimoli.pms.entity.Role;
import com.tolimoli.pms.entity.User;
import com.tolimoli.pms.exception.ResourceNotFoundException;
import com.tolimoli.pms.exception.BusinessLogicException;
import com.tolimoli.pms.repository.RoleRepository;
import com.tolimoli.pms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
        
        return user;
    }

    public User createUser(String username, String email, String password, String firstName, String lastName) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessLogicException("Username already exists: " + username);
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new BusinessLogicException("Email already exists: " + email);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // Assign default role
        Role defaultRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new BusinessLogicException("Default role not found"));
        user.addRole(defaultRole);

        return userRepository.save(user);
    }

    public User createUser(User user, Set<String> roleNames) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessLogicException("Username already exists: " + user.getUsername());
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessLogicException("Email already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign roles
        if (roleNames != null && !roleNames.isEmpty()) {
            for (String roleName : roleNames) {
                Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", roleName));
                user.addRole(role);
            }
        } else {
            // Assign default role
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new BusinessLogicException("Default role not found"));
            user.addRole(defaultRole);
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getActiveUsers() {
        return userRepository.findByEnabledTrue();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Check if username is being changed and if it already exists
        if (!user.getUsername().equals(updatedUser.getUsername()) && 
            userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new BusinessLogicException("Username already exists: " + updatedUser.getUsername());
        }

        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(updatedUser.getEmail()) && 
            userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new BusinessLogicException("Email already exists: " + updatedUser.getEmail());
        }

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());
        user.setDepartment(updatedUser.getDepartment());

        return userRepository.save(user);
    }

    public User changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessLogicException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User enableUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        user.setEnabled(true);
        return userRepository.save(user);
    }

    public User disableUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        user.setEnabled(false);
        return userRepository.save(user);
    }

    public User assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new ResourceNotFoundException("Role", roleName));

        user.addRole(role);
        return userRepository.save(user);
    }

    public User removeRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new ResourceNotFoundException("Role", roleName));

        user.removeRole(role);
        return userRepository.save(user);
    }

    public List<User> searchUsers(String searchTerm) {
        return userRepository.searchUsers(searchTerm);
    }

    public List<User> getUsersByRole(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }

    public boolean authenticateUser(String usernameOrEmail, String password) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isEnabled() && passwordEncoder.matches(password, user.getPassword())) {
                user.updateLastLogin();
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        userRepository.delete(user);
    }

    public Long countActiveUsers() {
        return userRepository.countActiveUsers();
    }
}