package com.user.event.streaming.service.helper;

import com.user.event.streaming.service.model.Event;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class EventStore {

    // 1. Thread-Safe ArrayList (Max 500)
    private final List<Event> eventQueue = Collections.synchronizedList(new ArrayList<>());

    // 2. Cache using a Map
    private final Map<String, List<Event>> simpleCache = Collections.synchronizedMap(new HashMap<>());

    public void addToQueue(Event event) {
        synchronized (eventQueue) {
            if (eventQueue.size() >= 500) {
                eventQueue.remove(0); // Remove oldest from ArrayList
            }
            eventQueue.add(event);
        }

        // Clear cache whenever new data arrives
        simpleCache.clear();
    }

    public List<Event> getQueue() {
        synchronized (eventQueue) {
            return new ArrayList<>(eventQueue);
        }
    }

    public void cacheResult(String key, List<Event> data) {
        if (simpleCache.size() > 50) {
            simpleCache.clear();
        }
        simpleCache.put(key, data);
    }

    public List<Event> getFromCache(String key) {
        return simpleCache.get(key);
    }
}
