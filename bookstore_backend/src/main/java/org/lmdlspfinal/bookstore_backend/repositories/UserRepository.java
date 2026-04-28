package org.lmdlspfinal.bookstore_backend.repositories;

import org.lmdlspfinal.bookstore_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}