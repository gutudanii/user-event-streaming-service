package com.user.event.streaming.service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {
    public Long id;
    public String name;
    public String username;
}
