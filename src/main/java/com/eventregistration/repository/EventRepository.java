package com.eventregistration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventregistration.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByCreatedBy(UUID uuid);

    List<Event> findByCategoryContainingIgnoreCase(String category);
}
