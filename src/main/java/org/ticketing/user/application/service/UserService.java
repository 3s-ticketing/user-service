package org.ticketing.user.application.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ticketing.common.exception.ConflictException;
import org.ticketing.common.exception.NotFoundException;
import org.ticketing.user.domain.entity.User;
import org.ticketing.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signUp(String email, String name, String phone) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new ConflictException("이미 존재하는 이메일입니다.");
        }

        User user = User.createGeneral(email, name, phone);

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
    public void deleteUser(UUID userId) {
        User user = findUser(userId);
        user.delete();
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}
