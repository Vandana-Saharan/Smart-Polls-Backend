package com.smartpolls.smartpollsapi.dto;

import java.util.Map;
import java.util.UUID;

public record PollResultsResponse(
        UUID pollId,
        Map<UUID, Long> votes
) {}
