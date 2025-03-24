package com.eventregistration.repository;

import com.eventregistration.entity.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail, UUID> {

    boolean existsByEmail(String email);

    Optional<UserEmail> findByEmail(String email);
}