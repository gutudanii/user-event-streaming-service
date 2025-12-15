package com.user.event.streaming.service.service.impl;

import com.user.event.streaming.service.dto.request.EventRequest;
import com.user.event.streaming.service.helper.EventStore;
import com.user.event.streaming.service.model.Event;
import com.user.event.streaming.service.repository.EventRepository;
import com.user.event.streaming.service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventStore eventStore;

    @Override
    public void processEvents(List<EventRequest> eventRequests) {
        for (EventRequest request : eventRequests) {
            // Map DTO to Entity
            Event event = Event.builder()
                    .userId(request.getUserId())
                    .eventType(request.getEventType())
                    .productId(request.getProductId())
                    .category(request.getCategory())
                    .timestamp(LocalDateTime.now())
                    .build();

            eventRepository.save(event);
            eventStore.addToQueue(event);
        }
    }

    @Override
    public List<Event> searchEvents(Long userId, String type, String cat, Long prodId) {
        String cacheKey = "search:" + userId + type + cat + prodId;

        // 1. Check Cache
        List<Event> cached = eventStore.getFromCache(cacheKey);
        if (cached != null) return cached;

        // 2. Check Queue (Search the 500 in-memory items)
        List<Event> results = eventStore.getQueue().stream()
                .filter(e -> (userId == null || e.getUserId().equals(userId)))
                .filter(e -> (type == null || e.getEventType().equalsIgnoreCase(type)))
                .filter(e -> (cat == null || e.getCategory().equalsIgnoreCase(cat)))
                .toList();

        if (results.isEmpty()) {
            results = eventRepository.searchEvents(userId, type, cat, prodId, null, null);
        }

        eventStore.cacheResult(cacheKey, results);
        return results;
    }

    @Override
    public List<Event> getRecent() {
        List<Event> all = eventStore.getQueue();
        int size = all.size();
        return all.subList(Math.max(size - 20, 0), size);
    }

    @Override
    public Map<String, Long> getSummary(String category) {
        return eventStore.getQueue().stream()
                .filter(e -> category == null || category.equalsIgnoreCase(e.getCategory()))
                .collect(Collectors.groupingBy(Event::getEventType, Collectors.counting()));
    }
}
