package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameOrEmail(String username, String email);
    List<User> findByIsActive(boolean status);
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.todoTasks WHERE u.isActive = true")
    List<User> findAllActiveWithTasks();

    Optional<User> findByExternalId(String externalId);
}
