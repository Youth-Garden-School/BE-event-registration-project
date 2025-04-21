package com.eventregistration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eventregistration.entity.EventAttendee;

@Repository
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, UUID> {
    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);

    boolean existsByEventIdAndEmail(UUID eventId, String email);

    List<EventAttendee> findByEventId(UUID eventId);

    Page<EventAttendee> findByEventId(UUID eventId, Pageable pageable);

    // Sử dụng EntityGraph để chỉ tải event mà không tải attendees của event
    @EntityGraph(
            attributePaths = {"event"},
            type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT ea FROM EventAttendee ea WHERE ea.user.id = :userId")
    Page<EventAttendee> findByUserIdWithoutAttendees(UUID userId, Pageable pageable);

    List<EventAttendee> findByUserId(UUID userId);

    Page<EventAttendee> findByUserId(UUID userId, Pageable pageable);
}
