package com.eventregistration.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventregistration.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.emails e WHERE e.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query(
            "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.emails e WHERE e.email = :email AND u.password IS NOT NULL AND u.password <> ''")
    boolean existsByEmailAndHasPassword(@Param("email") String email);
}
