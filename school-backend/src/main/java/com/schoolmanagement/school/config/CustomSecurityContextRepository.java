package com.schoolmanagement.school.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Custom SecurityContextRepository that can restore authentication from X-JSESSIONID header
 */
@Component
public class CustomSecurityContextRepository extends HttpSessionSecurityContextRepository {
    
    private final SessionRepository sessionRepository;
    
    public CustomSecurityContextRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    
    @Override
    public SecurityContext loadContext(HttpServletRequest request) {
        // First try to load from the standard session
        SecurityContext context = super.loadContext(request);
        
        // If no context, try to load from X-JSESSIONID header
        if (context == null || (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated())) {
            String sessionIdHeader = request.getHeader("X-JSESSIONID");
            if (sessionIdHeader != null && !sessionIdHeader.isEmpty()) {
                Session session = sessionRepository.findById(sessionIdHeader);
                if (session != null) {
                    // Try to restore authentication from session attributes
                    Object authAttribute = session.getAttribute("SPRING_SECURITY_CONTEXT");
                    if (authAttribute instanceof SecurityContext) {
                        context = (SecurityContext) authAttribute;
                        System.out.println("=== CustomSecurityContextRepository ===");
                        System.out.println("Restored authentication from X-JSESSIONID: " + sessionIdHeader);
                        System.out.println("Principal: " + context.getAuthentication().getName());
                        System.out.println("=========================================");
                    }
                }
            }
        }
        
        return context != null ? context : new SecurityContextImpl();
    }
    
    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        super.saveContext(context, request, response);
    }
}

