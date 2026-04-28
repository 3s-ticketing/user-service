package org.ticketing.user.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ticketing.user.domain.entity.User;

public interface JpaUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    List<User> findAllByDeletedAtIsNull();

    boolean existsByEmailAndDeletedAtIsNull(String email);
}
