package com.notas.microautenticacion.repository;

import com.notas.microautenticacion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = ?1")
    boolean existsByUsername(String username);
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = ?1")
    boolean existsByEmail(String email);
}
