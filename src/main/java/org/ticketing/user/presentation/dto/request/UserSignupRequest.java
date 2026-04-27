package org.ticketing.user.presentation.dto.request;

public record UserSignupRequest(
    String email,
    String name,
    String phone
) {}
