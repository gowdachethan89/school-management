package com.schoolmanagement.school.service;

import com.schoolmanagement.school.entity.User;
import com.schoolmanagement.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        // 1. Check if user exists
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already taken!";
        }

        // 2. Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Assign default role
        user.setRole("USER");

        userRepository.save(user);
        return "User registered successfully!";
    }
}
