package com.fivetpromart.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converts Keycloak JWT tokens to Spring Security authentication tokens.
 * 
 * Role Extraction Priority:
 * 1. realm_access.roles (standard Keycloak realm roles) - PRIMARY
 * 2. resource_access.{client-id}.roles (client-specific roles)
 * 3. account_type claim (if custom protocol mapper is configured)
 * 
 * Expected Roles (PascalCase, must match Keycloak realm configuration):
 * - Admin: Full system access
 * - Manager: Read access, limited write
 * - SalesStaff: Order and customer operations
 * - WarehouseStaff: Inventory and stock operations
 */
@Slf4j
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        log.debug("JWT Claims: {}", jwt.getClaims());
        log.debug("Extracted Authorities: {}", authorities);

        String principalName = extractPrincipalName(jwt);
        log.debug("Principal Name: {}", principalName);

        return new JwtAuthenticationToken(jwt, authorities, principalName);
    }

    /**
     * Extract principal name to use as user identifier.
     * Priority: sub (Keycloak UUID) > preferred_username > email
     */
    private String extractPrincipalName(Jwt jwt) {
        // Primary: Keycloak User ID (UUID) from "sub" claim
        String sub = jwt.getSubject();
        if (sub != null && !sub.isEmpty()) {
            return sub;
        }

        // Fallback: preferred_username
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        if (preferredUsername != null && !preferredUsername.isEmpty()) {
            log.debug("Using 'preferred_username' as principal: {}", preferredUsername);
            return preferredUsername;
        }

        // Fallback: email
        String email = jwt.getClaimAsString("email");
        if (email != null && !email.isEmpty()) {
            log.debug("Using 'email' as principal: {}", email);
            return email;
        }

        log.warn("Cannot extract principal name from JWT, using 'sub' as default");
        return jwt.getSubject();
    }

    /**
     * Extract roles from Keycloak JWT token.
     * 
     * Keycloak stores roles in:
     * - realm_access.roles (realm-level roles)
     * - resource_access.{client-id}.roles (client-level roles)
     * 
     * Roles are converted to Spring Security authorities with "ROLE_" prefix.
     * Example: "Admin" -> "ROLE_Admin"
     */
    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        // Priority 1: realm_access.roles (standard Keycloak realm roles)
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
            log.debug("Found realm roles: {}", realmRoles);
            return realmRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
        }

        // Priority 2: resource_access.{client-id}.roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            for (Object clientRoles : resourceAccess.values()) {
                if (clientRoles instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> clientRolesMap = (Map<String, Object>) clientRoles;
                    if (clientRolesMap.containsKey("roles")) {
                        @SuppressWarnings("unchecked")
                        Collection<String> roles = (Collection<String>) clientRolesMap.get("roles");
                        log.debug("Found resource roles: {}", roles);
                        return roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toSet());
                    }
                }
            }
        }

        // Priority 3: account_type claim (requires custom Keycloak protocol mapper)
        // NOTE: This is a fallback for future use. To enable:
        // 1. Add a "User Attribute" protocol mapper in Keycloak
        // 2. Map user attribute "accountType" to token claim "account_type"
        String accountType = jwt.getClaim("account_type");
        if (accountType != null) {
            log.debug("Using account_type as role: {}", accountType);
            return Stream.of(new SimpleGrantedAuthority("ROLE_" + accountType))
                    .collect(Collectors.toList());
        }

        log.warn("No roles found in JWT token! User will have no authorities.");
        return Stream.<GrantedAuthority>empty().collect(Collectors.toSet());
    }
}