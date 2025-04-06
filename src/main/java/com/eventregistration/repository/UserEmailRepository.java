package com.eventregistration.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventregistration.entity.UserEmail;

@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail, UUID> {

    boolean existsByEmail(String email);

    Optional<UserEmail> findByEmail(String email);
}
