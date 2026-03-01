package com.smartpolls.smartpollsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VoteRequest(
        @NotNull UUID optionId,
        @NotBlank String voterId
) {}
