package com.example.app.service;

import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.debug("SERVICE - getAllUsers() called");
        try {
            List<User> users = userRepository.findAll();
            log.info("SERVICE - Found {} users in database", users.size());
            return users;
        } catch (Exception e) {
            log.error("SERVICE - Error retrieving all users", e);
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.debug("SERVICE - getUserById() called with id: {}", id);
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                log.info("SERVICE - User found with id: {}", id);
            } else {
                log.warn("SERVICE - User not found with id: {}", id);
            }
            return user;
        } catch (Exception e) {
            log.error("SERVICE - Error retrieving user by id: {}", id, e);
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        log.debug("SERVICE - getUserByUsername() called with username: {}", username);
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                log.info("SERVICE - User found with username: {}", username);
            } else {
                log.warn("SERVICE - User not found with username: {}", username);
            }
            return user;
        } catch (Exception e) {
            log.error("SERVICE - Error retrieving user by username: {}", username, e);
            throw e;
        }
    }
    
    @Transactional
    public User createUser(User user) {
        log.info("SERVICE - createUser() called for username: {}", user.getUsername());
        
        try {
            // Validaciones
            if (userRepository.existsByUsername(user.getUsername())) {
                log.error("SERVICE - Username already exists: {}", user.getUsername());
                throw new IllegalArgumentException("Username already exists: " + user.getUsername());
            }
            
            if (userRepository.existsByEmail(user.getEmail())) {
                log.error("SERVICE - Email already exists: {}", user.getEmail());
                throw new IllegalArgumentException("Email already exists: " + user.getEmail());
            }
            
            log.debug("SERVICE - Saving user to database: {}", user.getUsername());
            User savedUser = userRepository.save(user);
            log.info("SERVICE - User created successfully with ID: {}", savedUser.getId());
            return savedUser;
            
        } catch (Exception e) {
            log.error("SERVICE - Error creating user: {}", user.getUsername(), e);
            throw e;
        }
    }
    
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        log.info("SERVICE - updateUser() called for id: {}", id);
        
        try {
            User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("SERVICE - User not found for update with id: {}", id);
                    return new IllegalArgumentException("User not found with id: " + id);
                });
            
            log.debug("SERVICE - Updating user fields for: {}", existingUser.getUsername());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            
            User savedUser = userRepository.save(existingUser);
            log.info("SERVICE - User updated successfully: {}", savedUser.getId());
            return savedUser;
            
        } catch (Exception e) {
            log.error("SERVICE - Error updating user with id: {}", id, e);
            throw e;
        }
    }
    
    @Transactional
    public void deleteUser(Long id) {
        log.info("SERVICE - deleteUser() called for id: {}", id);
        
        try {
            if (!userRepository.existsById(id)) {
                log.error("SERVICE - User not found for deletion with id: {}", id);
                throw new IllegalArgumentException("User not found with id: " + id);
            }
            
            userRepository.deleteById(id);
            log.info("SERVICE - User deleted successfully with id: {}", id);
            
        } catch (Exception e) {
            log.error("SERVICE - Error deleting user with id: {}", id, e);
            throw e;
        }
    }
}
