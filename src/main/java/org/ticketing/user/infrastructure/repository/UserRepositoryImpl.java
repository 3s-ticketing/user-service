package org.ticketing.user.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.ticketing.user.domain.entity.User;
import org.ticketing.user.domain.repository.UserRepository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findById(UUID userId) {
        return jpaUserRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }
}
