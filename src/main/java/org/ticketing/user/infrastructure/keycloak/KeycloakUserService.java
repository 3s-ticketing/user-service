package org.ticketing.user.infrastructure.keycloak;

import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.ticketing.common.exception.ConflictException;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public void createUser(String email, String password, String name) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(name);
        user.setLastName("User");
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRequiredActions(List.of());

        Response response = keycloak.realm(realm)
            .users()
            .create(user);

        int status = response.getStatus();

        if (status == 409) {
            response.close();
            throw new ConflictException("이미 Keycloak에 존재하는 사용자입니다.");
        }

        if (status != 201) {
            response.close();
            throw new IllegalStateException("Keycloak 사용자 생성 실패. status=" + status);
        }

        String userId = extractCreatedUserId(response.getLocation());
        response.close();

        setPassword(userId, password);
        clearRequiredActions(userId, name);
    }

    private void setPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm(realm)
            .users()
            .get(userId)
            .resetPassword(credential);
    }

    private void clearRequiredActions(String userId, String name) {
        UserRepresentation createdUser = keycloak.realm(realm)
            .users()
            .get(userId)
            .toRepresentation();

        createdUser.setEnabled(true);
        createdUser.setEmailVerified(true);
        createdUser.setFirstName(name);
        createdUser.setLastName("User");
        createdUser.setRequiredActions(List.of());

        keycloak.realm(realm)
            .users()
            .get(userId)
            .update(createdUser);
    }

    private String extractCreatedUserId(URI location) {
        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}