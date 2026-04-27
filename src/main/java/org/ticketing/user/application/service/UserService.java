package org.ticketing.user.application.service;

import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ticketing.user.domain.entity.User;
import org.ticketing.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signUp(String email, String name, String phone) {

        userRepository.findByEmail(email)
            .ifPresent(u -> { throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });

        User user = User.createGeneral(email, name, phone);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
    }
}
