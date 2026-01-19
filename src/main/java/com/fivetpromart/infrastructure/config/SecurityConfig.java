package com.fivetpromart.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Set;

/**
 * Security configuration for 5TProMart.
 * 
 * Development Security Bypass:
 * - Set SECURITY_BYPASS=true to grant all roles to authenticated users
 * - Enables unrestricted endpoint testing during development
 * - Every user receives Admin, Manager, SalesStaff, and WarehouseStaff roles
 * 
 * WARNING: This feature must NEVER be enabled in production environments.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${security.bypass-enabled:false}")
    private boolean securityBypassEnabled;

    private final JwtAuthConverter jwtAuthConverter;

    /** All roles in the system - granted to all users when security bypass is enabled */
    private static final Set<SimpleGrantedAuthority> ALL_ROLES = Set.of(
            new SimpleGrantedAuthority("ROLE_Admin"),
            new SimpleGrantedAuthority("ROLE_Manager"),
            new SimpleGrantedAuthority("ROLE_SalesStaff"),
            new SimpleGrantedAuthority("ROLE_WarehouseStaff")
    );

    @PostConstruct
    public void logSecurityMode() {
        if (securityBypassEnabled) {
            log.warn("╔════════════════════════════════════════════════════════════╗");
            log.warn("║  ⚠️  DEVELOPMENT MODE: Security Bypass Active             ║");
            log.warn("║  All authenticated users granted full role access         ║");
            log.warn("║  This configuration is NOT safe for production use        ║");
            log.warn("╚════════════════════════════════════════════════════════════╝");
        } else {
            log.info("Security: Role-based authorization ENABLED");
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**", "/api/v1/signup/**", "/error", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(this::convertJwt)
                        )
                );

        return http.build();
    }

    /**
     * Convert JWT to Authentication, optionally granting all roles in GOD MODE.
     */
    private JwtAuthenticationToken convertJwt(Jwt jwt) {
        JwtAuthenticationToken normalAuth = (JwtAuthenticationToken) jwtAuthConverter.convert(jwt);
        
        if (securityBypassEnabled && normalAuth != null) {
            // Development bypass: Grant all roles for unrestricted testing
            var allAuthorities = new java.util.HashSet<>(normalAuth.getAuthorities());
            allAuthorities.addAll(ALL_ROLES);
            
            log.debug("Security bypass active: Granting all roles to user {}", normalAuth.getName());
            return new JwtAuthenticationToken(jwt, allAuthorities, normalAuth.getName());
        }
        
        return normalAuth;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
