package com.eventregistration.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventregistration.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, UUID> {
    
}
