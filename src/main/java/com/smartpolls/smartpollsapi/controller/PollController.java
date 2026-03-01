package com.smartpolls.smartpollsapi.controller;

import com.smartpolls.smartpollsapi.dto.CreatePollRequest;
import com.smartpolls.smartpollsapi.dto.PollResponse;
import com.smartpolls.smartpollsapi.service.PollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
@CrossOrigin // temporary, we’ll tighten later
public class PollController {

    private final PollService pollService;

    @PostMapping
    public PollResponse createPoll(@Valid @RequestBody CreatePollRequest request) {
        return pollService.createPoll(request);
    }

    @GetMapping
    public List<PollResponse> listPolls() {
        return pollService.listPolls();
    }

    @GetMapping("/{pollId}")
    public PollResponse getPoll(@PathVariable UUID pollId) {
        return pollService.getPoll(pollId);
    }

    @DeleteMapping("/{pollId}")
    public void deletePoll(@PathVariable UUID pollId) {
        pollService.deletePoll(pollId);
    }
}
