package org.ticketing.user.infrastructure.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@Profile("!test")
public class UserSecurityConfig {

    private static final String[] PERMIT_ALL_URLS = {
        "/error",
        "/actuator/health",

        // Swagger 사용 시
        "/swagger-ui/**",
        "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin(formLogin -> formLogin.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PERMIT_ALL_URLS).permitAll()

                // 회원가입
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                // 로그인
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                // 내부 서비스 호출
                .requestMatchers("/internal/users/**").permitAll()

                // 그 외 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter()))
            );

        return http.build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> keycloakJwtAuthenticationConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.get("roles") instanceof List<?> roles) {
                roles.forEach(role -> authorities.add(
                    new SimpleGrantedAuthority("ROLE_" + role)
                ));
            }

            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null) {
                resourceAccess.values().forEach(resource -> {
                    if (resource instanceof Map<?, ?> resourceMap
                        && resourceMap.get("roles") instanceof List<?> roles) {
                        roles.forEach(role -> authorities.add(
                            new SimpleGrantedAuthority("ROLE_" + role)
                        ));
                    }
                });
            }

            return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
        };
    }
}