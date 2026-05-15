package org.ticketing.user.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import org.ticketing.user.domain.enums.UserStatus;

public record UserUpdateStatusRequest(

    @NotNull(message = "변경할 회원 상태는 필수입니다.")
    UserStatus status
) {}
