package com.smartpolls.smartpollsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreatePollRequest(

        @NotBlank
        @Size(min = 5)
        String question,

        @Size(min = 2, max = 6)
        List<@NotBlank String> options
) {
}
