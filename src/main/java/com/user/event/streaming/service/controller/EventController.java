package com.user.event.streaming.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.event.streaming.service.dto.request.EventRequest;
import com.user.event.streaming.service.model.Event;
import com.user.event.streaming.service.service.EventService;
import com.user.event.streaming.service.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Response> createEvents(@RequestBody Object input) {
        try {
            List<EventRequest> eventRequests;

            if (input instanceof List) {
                eventRequests = objectMapper.convertValue(input,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, EventRequest.class));
            } else {
                EventRequest single = objectMapper.convertValue(input, EventRequest.class);
                eventRequests = List.of(single);
            }

            eventService.processEvents(eventRequests);

            return ResponseEntity.ok(Response.builder()
                    .message("Events processed successfully")
                    .success(true)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.builder()
                    .message("Invalid data format: " + e.getMessage())
                    .success(false)
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<Response> searchEvents(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long productId) {

        List<Event> results = eventService.searchEvents(userId, eventType, category, productId);

        return ResponseEntity.ok(Response.builder()
                .message("Events retrieved successfully")
                .success(true)
                .object(results)
                .build());
    }

    @GetMapping("/recent")
    public ResponseEntity<Response> getRecentEvents() {
        List<Event> recent = eventService.getRecent();

        return ResponseEntity.ok(Response.builder()
                .message("Recent activity fetched")
                .success(true)
                .object(recent)
                .build());
    }

    @GetMapping("/summary")
    public ResponseEntity<Response> getEventSummary(@RequestParam(required = false) String category) {
        Map<String, Long> summary = eventService.getSummary(category);

        return ResponseEntity.ok(Response.builder()
                .message("Event summary generated")
                .success(true)
                .object(summary)
                .build());
    }
}
