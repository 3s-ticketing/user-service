package org.ticketing.user.presentation.controller.external;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.ticketing.user.application.service.UserService;
import org.ticketing.user.presentation.dto.request.UserSignupRequest;
import org.ticketing.user.presentation.dto.request.UserUpdateRequest;
import org.ticketing.user.presentation.dto.response.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signUp(@Valid @RequestBody UserSignupRequest request) {
        return UserResponse.from(
            userService.signUp(
                request.email(),
                request.password(),
                request.name(),
                request.phone()
            )
        );
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable UUID userId) {
        return UserResponse.from(
            userService.getUser(userId)
        );
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getUsers()
            .stream()
            .map(UserResponse::from)
            .toList();
    }

    @PatchMapping("/{userId}")
    public UserResponse updateUser(
        @PathVariable UUID userId,
        @Valid @RequestBody UserUpdateRequest request
    ) {
        return UserResponse.from(
            userService.updateUser(
                userId,
                request.name(),
                request.phone()
            )
        );
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}
