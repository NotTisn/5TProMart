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

        log.info("JWT Claims: {}", jwt.getClaims());
        log.info("Extracted Authorities: {}", authorities);

        // ✅ Extract principal name (staffId) from JWT
        String principalName = extractPrincipalName(jwt);
        log.info("Principal Name (staffId): {}", principalName);

        // ✅ Create JwtAuthenticationToken with custom principal name
        return new JwtAuthenticationToken(jwt, authorities, principalName);
    }

    /**
     * ✅ Extract principal name to use as staffId
     * Priority: sub (Keycloak UUID) > preferred_username > email
     */
    private String extractPrincipalName(Jwt jwt) {
        // Option 1: Dùng Keycloak User ID (UUID) từ "sub"
        String sub = jwt.getSubject();
        if (sub != null && !sub.isEmpty()) {
            log.info("Using 'sub' as staffId: {}", sub);
            return sub;
        }

        // Option 2: Fallback to preferred_username
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        if (preferredUsername != null && !preferredUsername.isEmpty()) {
            log.info("Using 'preferred_username' as staffId: {}", preferredUsername);
            return preferredUsername;
        }

        // Option 3: Fallback to email
        String email = jwt.getClaimAsString("email");
        if (email != null && !email.isEmpty()) {
            log.info("Using 'email' as staffId: {}", email);
            return email;
        }

        // Option 4: Custom claim (nếu bạn config trong Keycloak)
        String staffId = jwt.getClaimAsString("staff_id");
        if (staffId != null && !staffId.isEmpty()) {
            log.info("Using 'staff_id' claim: {}", staffId);
            return staffId;
        }

        log.warn("Cannot extract principal name from JWT, using 'sub' as default");
        return jwt.getSubject();
    }

    /**
     * Extract roles from Keycloak JWT token
     * Keycloak stores roles in: realm_access.roles or resource_access.{client-id}.roles
     */
    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        log.info("Extracting roles from JWT...");

        // Try to get realm_access roles first
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        log.info("realm_access claim: {}", realmAccess);

        if (realmAccess != null && realmAccess.containsKey("roles")) {
            Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
            log.info("Found realm roles: {}", realmRoles);
            return realmRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
        }

        // Try to get resource_access roles (if realm_access not found)
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        log.info("resource_access claim: {}", resourceAccess);

        if (resourceAccess != null) {
            // Get the first client's roles
            for (Object clientRoles : resourceAccess.values()) {
                if (clientRoles instanceof Map) {
                    Map<String, Object> clientRolesMap = (Map<String, Object>) clientRoles;
                    if (clientRolesMap.containsKey("roles")) {
                        Collection<String> roles = (Collection<String>) clientRolesMap.get("roles");
                        log.info("Found resource roles: {}", roles);
                        return roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toSet());
                    }
                }
            }
        }

        // Also check for account_type claim (from your database)
        String accountType = jwt.getClaim("account_type");
        log.info("account_type claim: {}", accountType);

        if (accountType != null) {
            log.info("Using account_type as role: {}", accountType);
            return Stream.of(new SimpleGrantedAuthority("ROLE_" + accountType))
                    .collect(Collectors.toList());
        }

        log.warn("No roles found in JWT token!");
        return Stream.<GrantedAuthority>empty().collect(Collectors.toSet());
    }
}