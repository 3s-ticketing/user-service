package org.ticketing.user.infrastructure.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class KeycloakConfig {

    @Bean
    public Keycloak keycloakAdminClient(
        @Value("${keycloak.server-url}") String serverUrl,
        @Value("${keycloak.admin-realm}") String adminRealm,
        @Value("${keycloak.admin-client-id}") String adminClientId,
        @Value("${keycloak.admin-username}") String adminUsername,
        @Value("${keycloak.admin-password}") String adminPassword
    ) {
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(adminRealm)
            .clientId(adminClientId)
            .username(adminUsername)
            .password(adminPassword)
            .grantType(OAuth2Constants.PASSWORD)
            .build();
    }
}