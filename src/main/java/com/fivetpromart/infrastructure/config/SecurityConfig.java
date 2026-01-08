package com.fivetpromart.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // <-- THÊM TỪ FILE MỚI
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
// (Import cho CORS)
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // <-- THÊM TỪ FILE MỚI: Kích hoạt @PreAuthorize
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. (TỪ FILE MỚI) Kích hoạt CORS bằng bean ở dưới
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Tắt CSRF (vì chúng ta dùng API stateless)
                .csrf(csrf -> csrf.disable())

                // 3. Cấu hình Session STATELESS (không lưu session)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Cấu hình ủy quyền (Authorization)
                .authorizeHttpRequests(authorize -> authorize
                        // (Đây là các endpoint ĐÚNG cho project của bạn)
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/refresh-token").permitAll()
                        .requestMatchers("/api/v1/signup/**").permitAll()

                        // (Tùy chọn) Mở Swagger (nếu bạn dùng)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // 3b. Tất cả các đường dẫn /api/ còn lại PHẢI xác thực
                        .requestMatchers("/api/**").authenticated()

                        // 3c. Bất kỳ request nào khác (ví dụ: "/") cũng mở
                        .anyRequest().permitAll()
                )

                // 5. Cấu hình là một Resource Server (để xác thực JWT)
                // (Không cần converter phức tạp, .jwtDecoder(jwtDecoder()) là đủ)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))
                );

        return http.build();
    }

    // *** (TỪ FILE MỚI) ĐỊNH NGHĨA BEAN CORS ***
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // (Đây là cấu hình an toàn hơn cho dev)
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Cho phép mọi header
        configuration.setAllowCredentials(true); // Cho phép cookie (nếu cần)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cho mọi đường dẫn
        return source;
    }

    // *** (TỪ FILE CŨ) ĐỊNH NGHĨA BEAN JWTDECODER ***
    @Bean
    public JwtDecoder jwtDecoder() {
        // Đây là "best practice" cho Spring Boot 3
        return JwtDecoders.fromOidcIssuerLocation(issuerUri);
    }
}