package org.ticketing.user.infrastructure.repository;

import java.util.List;
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
        return jpaUserRepository.findByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public boolean existsByEmailAndDeletedAtIsNull(String email) {
        return jpaUserRepository.existsByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }
}
