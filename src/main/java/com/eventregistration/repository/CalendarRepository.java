package com.eventregistration.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventregistration.entity.Calendar;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, UUID> {
    
    List<Calendar> findByUserId(UUID userId);
    
    Optional<Calendar> findByIdAndUserId(UUID id, UUID userId);
}
