package com.user.event.streaming.service.service;

import com.user.event.streaming.service.dto.request.EventRequest;
import com.user.event.streaming.service.model.Event;

import java.util.List;
import java.util.Map;

public interface EventService {
    void processEvents(List<EventRequest> eventRequests);

    List<Event> searchEvents(Long userId, String type, String cat, Long prodId);

    List<Event> getRecent();

    Map<String, Long> getSummary(String category);
}
