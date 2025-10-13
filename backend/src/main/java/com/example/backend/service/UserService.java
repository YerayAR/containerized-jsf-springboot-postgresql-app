package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
    /*
     * UserService is a service class that implements UserDetailsService.
     * It is responsible for loading user details from the UserRepository.
     */
@Service
public class UserService implements UserDetailsService {
    
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    //  This method loads user details by username.
    // It uses the UserRepository to find the user and builds a UserDetails object.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by username and map it to UserDetails
        return repository.findByUsername(username)
                // If user is found, map it to UserDetails
                .map(u -> org.springframework.security.core.userdetails.User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .roles(u.getRole())
                        .build())
                        // If user is not found, throw UsernameNotFoundException
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
