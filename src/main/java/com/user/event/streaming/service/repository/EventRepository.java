package com.user.event.streaming.service.repository;

import com.user.event.streaming.service.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE " +
            "(:userId IS NULL OR e.userId = :userId) AND " +
            "(:eventType IS NULL OR e.eventType = :eventType) AND " +
            "(:category IS NULL OR e.category = :category) AND " +
            "(:productId IS NULL OR e.productId = :productId) AND " +
            "(:from IS NULL OR e.timestamp >= :from) AND " +
            "(:to IS NULL OR e.timestamp <= :to)")
    List<Event> searchEvents(
            @Param("userId") Long userId,
            @Param("eventType") String eventType,
            @Param("category") String category,
            @Param("productId") Long productId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}