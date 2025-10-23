package com.example.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        log.debug("PrePersist - Creating new user: {}", username);
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
            log.debug("PrePersist - Set createdAt to: {}", createdAt);
        }
    }
    
    @PostPersist
    public void postPersist() {
        log.info("PostPersist - User created successfully with ID: {}", id);
    }
    
    @PostLoad
    public void postLoad() {
        log.debug("PostLoad - User loaded from database: {} (ID: {})", username, id);
    }
    
    @PreUpdate
    public void preUpdate() {
        log.debug("PreUpdate - Updating user: {} (ID: {})", username, id);
    }
    
    @PostUpdate
    public void postUpdate() {
        log.info("PostUpdate - User updated successfully: {} (ID: {})", username, id);
    }
    
    @PreRemove
    public void preRemove() {
        log.warn("PreRemove - Deleting user: {} (ID: {})", username, id);
    }
    
    @PostRemove
    public void postRemove() {
        log.info("PostRemove - User deleted: {}", username);
    }
}
