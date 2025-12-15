package com.user.event.streaming.service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListResponse<T> {
    public boolean success;
    public String message;
    public List<T> object;
}
