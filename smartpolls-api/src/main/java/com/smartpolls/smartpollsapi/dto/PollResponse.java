package com.smartpolls.smartpollsapi.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PollResponse(
        UUID id,
        String question,
        Instant createdAt,
        List<PollOptionResponse> options
) {
}
