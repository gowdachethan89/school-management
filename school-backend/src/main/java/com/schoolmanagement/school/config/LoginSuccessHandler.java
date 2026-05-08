package com.schoolmanagement.school.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        
        // Get the session ID from the request
        String sessionId = request.getSession().getId();

        Map<String, Object> data = new HashMap<>();
        data.put("message", "Login successful");
        data.put("username", authentication.getName());
        data.put("sessionId", sessionId);
        
        // Explicitly set JSESSIONID header for client to use if cookies fail
        response.setHeader("X-JSESSIONID", sessionId);
        
        System.out.println("=== LOGIN SUCCESS ===");
        System.out.println("Username: " + authentication.getName());
        System.out.println("Session ID: " + sessionId);
        System.out.println("====================");

        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
