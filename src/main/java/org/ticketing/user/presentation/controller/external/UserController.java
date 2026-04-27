package org.ticketing.user.presentation.controller.external;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ticketing.user.application.service.UserService;
import org.ticketing.user.presentation.dto.request.UserSignupRequest;
import org.ticketing.user.presentation.dto.response.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> signUp(@RequestBody UserSignupRequest request) {
        return ResponseEntity.ok(
            UserResponse.from(
                userService.signUp(
                    request.email(),
                    request.name(),
                    request.phone()
                )
            )
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(
            UserResponse.from(
                userService.getUser(userId)
            )
        );
    }
}