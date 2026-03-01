package com.smartpolls.smartpollsapi.dto;

import java.util.UUID;

public record PollOptionResponse(
        UUID id,
        String text
) {
}
