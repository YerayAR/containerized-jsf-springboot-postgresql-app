package com.example.app.controller;

import com.example.app.model.User;
import com.example.app.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Data
@Component
@ViewScoped
@RequiredArgsConstructor
public class UserBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final UserService userService;
    
    private List<User> users;
    private User selectedUser;
    private User newUser;
    
    @PostConstruct
    public void init() {
        log.info("BEAN - UserBean initialized");
        newUser = new User();
        loadUsers();
    }
    
    public void loadUsers() {
        log.debug("BEAN - loadUsers() called");
        try {
            users = userService.getAllUsers();
            log.info("BEAN - Loaded {} users", users.size());
        } catch (Exception e) {
            log.error("BEAN - Error loading users", e);
            addErrorMessage("Error loading users: " + e.getMessage());
        }
    }
    
    public void createUser() {
        log.info("BEAN - createUser() called for username: {}", newUser.getUsername());
        try {
            userService.createUser(newUser);
            log.info("BEAN - User created successfully: {}", newUser.getUsername());
            addInfoMessage("User created successfully!");
            
            newUser = new User(); // Reset form
            loadUsers(); // Refresh list
            
        } catch (IllegalArgumentException e) {
            log.warn("BEAN - Validation error creating user: {}", e.getMessage());
            addErrorMessage(e.getMessage());
        } catch (Exception e) {
            log.error("BEAN - Unexpected error creating user", e);
            addErrorMessage("Error creating user: " + e.getMessage());
        }
    }
    
    public void updateUser() {
        log.info("BEAN - updateUser() called for user ID: {}", selectedUser.getId());
        try {
            userService.updateUser(selectedUser.getId(), selectedUser);
            log.info("BEAN - User updated successfully: {}", selectedUser.getId());
            addInfoMessage("User updated successfully!");
            loadUsers();
            
        } catch (Exception e) {
            log.error("BEAN - Error updating user", e);
            addErrorMessage("Error updating user: " + e.getMessage());
        }
    }
    
    public void deleteUser(User user) {
        log.info("BEAN - deleteUser() called for user ID: {}", user.getId());
        try {
            userService.deleteUser(user.getId());
            log.info("BEAN - User deleted successfully: {}", user.getId());
            addInfoMessage("User deleted successfully!");
            loadUsers();
            
        } catch (Exception e) {
            log.error("BEAN - Error deleting user", e);
            addErrorMessage("Error deleting user: " + e.getMessage());
        }
    }
    
    private void addInfoMessage(String message) {
        log.debug("BEAN - Adding info message: {}", message);
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        log.debug("BEAN - Adding error message: {}", message);
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
}
