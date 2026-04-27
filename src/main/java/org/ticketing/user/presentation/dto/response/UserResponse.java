package org.ticketing.user.presentation.dto.response;

import java.util.UUID;
import org.ticketing.user.domain.entity.User;

public record UserResponse(
   UUID userId,
   String email,
    String name,
    String phone
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getEmail(),
            user.getName(),
            user.getPhone()
        );
    }
}
