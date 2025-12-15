package com.user.event.streaming.service.dto.request;

import lombok.Data;

@Data
public class EventRequest {
    private Long userId;
    private String eventType; // VIEW, ADD_TO_CART, PURCHASE
    private Long productId;
    private String category;
}
