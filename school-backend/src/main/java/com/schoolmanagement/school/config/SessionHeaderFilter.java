package com.schoolmanagement.school.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to handle X-JSESSIONID header as fallback for session identification in cross-domain requests
 */
@Component
public class SessionHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Check if X-JSESSIONID header is present
        String sessionIdHeader = request.getHeader("X-JSESSIONID");
        
        if (sessionIdHeader != null && !sessionIdHeader.isEmpty()) {
            // Check if JSESSIONID cookie already exists
            Cookie[] cookies = request.getCookies();
            boolean jsessionidExists = false;
            
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("JSESSIONID".equals(cookie.getName())) {
                        jsessionidExists = true;
                        break;
                    }
                }
            }
            
            // If no cookie exists, add the one from header
            if (!jsessionidExists) {
                // Create a wrapper request to inject the JSESSIONID cookie
                request = new SessionIdWrapper(request, sessionIdHeader);
                System.out.println("Applied X-JSESSIONID header as cookie: " + sessionIdHeader);
            }
        }
        
        // Set CORS response headers
        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-JSESSIONID");
        response.setHeader("Access-Control-Expose-Headers", "X-JSESSIONID");
        
        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Wrapper class to inject JSESSIONID cookie from header
     */
    private static class SessionIdWrapper extends jakarta.servlet.http.HttpServletRequestWrapper {
        private final String sessionId;
        
        public SessionIdWrapper(HttpServletRequest request, String sessionId) {
            super(request);
            this.sessionId = sessionId;
        }
        
        @Override
        public String getRequestedSessionId() {
            return sessionId;
        }
        
        @Override
        public boolean isRequestedSessionIdValid() {
            return true;
        }
    }
}

