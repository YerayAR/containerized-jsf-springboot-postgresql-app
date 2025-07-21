package com.example.backend;

// JPA standard annotations with Hibernate enhancements
import javax.persistence.*;

// Hibernate-specific optimizations
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

// Swagger documentation imports
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "users")
@DynamicUpdate
@SelectBeforeUpdate
@Schema(description = "User entity for authentication")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Column(unique = true)
    @Schema(description = "Username for authentication", example = "admin")
    private String username;

    private String password;

    private String role;

    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
