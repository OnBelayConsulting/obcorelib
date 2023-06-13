package com.onbelay.core.auth.serviceimpl;

import com.onbelay.core.auth.model.OBUser;
import com.onbelay.core.auth.service.ApplicationAuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ApplicationAuthenticationServiceBean implements ApplicationAuthenticationService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public OBUser getCurrentUser() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String name = authentication.getName();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
            Jwt jwt = token.getToken();
            name = (String) jwt.getClaims().get("name");
            for (String claim: jwt.getClaims().keySet()) {
                logger.error("Claim name: " + claim);
            }
        }

        Collection<? extends GrantedAuthority> authorities =  authentication.getAuthorities();
        return new OBUser(
                name,
                authorities);
    }

}
