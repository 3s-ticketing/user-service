package org.ticketing.user.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.ticketing.user.domain.entity.User;

public interface UserRepository {

    Optional<User> findById(UUID userId);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    boolean existsByEmailAndDeletedAtIsNull(String email);

    User save(User user);
}
