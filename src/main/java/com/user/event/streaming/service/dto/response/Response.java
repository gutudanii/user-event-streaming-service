package com.user.event.streaming.service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {
    public boolean success;
    public String message;
    public Object object;
}
