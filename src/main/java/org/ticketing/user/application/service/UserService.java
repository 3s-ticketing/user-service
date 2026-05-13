package org.ticketing.user.application.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ticketing.common.exception.ConflictException;
import org.ticketing.common.exception.NotFoundException;
import org.ticketing.user.domain.entity.User;
import org.ticketing.user.domain.enums.UserRole;
import org.ticketing.user.domain.enums.UserStatus;
import org.ticketing.user.domain.repository.UserRepository;
import org.ticketing.user.infrastructure.keycloak.KeycloakUserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;

    @Transactional
    public User signUp(String email, String password, String name, String phone, UserRole role) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new ConflictException("이미 존재하는 이메일입니다.");
        }

        UserRole signUpRole = role == null ? UserRole.GENERAL : role;

        if (signUpRole == UserRole.ADMIN) {
            throw new IllegalArgumentException("관리자 계정은 직접 가입할 수 없습니다.");
        }

        keycloakUserService.createUser(email, password, name);

        User user = switch (signUpRole) {
            case GENERAL -> User.createGeneral(email, name, phone);
            case CLUB_ADMIN -> User.createClubAdmin(email, name, phone);
            case ADMIN -> throw new IllegalArgumentException("관리자 계정은 직접 가입할 수 없습니다.");
        };

        return userRepository.save(user);
    }

    public User getUser(UUID userId) {
        return findUser(userId);
    }

    public boolean existsById(UUID userId) {
        return userRepository.findById(userId).isPresent();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(UUID userId, String name, String phone) {
        User user = findUser(userId);
        user.update(name, phone);
        return user;
    }

    @Transactional
    public void updateUserStatus(UUID userId, UserStatus status) {
        User user = findUser(userId);

        if (user.getRole() != UserRole.CLUB_ADMIN) {
            throw new IllegalArgumentException("클럽 관리자 신청자만 승인 또는 거절할 수 있습니다.");
        }

        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalArgumentException("대기 상태의 클럽 관리자 신청만 처리할 수 있습니다.");
        }

        if (status != UserStatus.APPROVED && status != UserStatus.REJECTED) {
            throw new IllegalArgumentException("가입 상태 변경은 APPROVED 또는 REJECTED만 가능합니다.");
        }

        if (status == UserStatus.APPROVED) {
            user.approve();
        } else {
            user.reject();
        }
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = findUser(userId);
        user.delete();
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}
