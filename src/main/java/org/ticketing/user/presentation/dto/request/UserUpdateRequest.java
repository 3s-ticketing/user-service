package org.ticketing.user.presentation.dto.request;

import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(
    String name,

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식은 010-0000-0000 이어야 합니다.")
    String phone
) {}
