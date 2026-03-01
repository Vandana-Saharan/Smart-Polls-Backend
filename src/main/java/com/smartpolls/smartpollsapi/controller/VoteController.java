package com.smartpolls.smartpollsapi.controller;

import com.smartpolls.smartpollsapi.dto.PollResultsResponse;
import com.smartpolls.smartpollsapi.dto.VoteRequest;
import com.smartpolls.smartpollsapi.dto.VoteResponse;
import com.smartpolls.smartpollsapi.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/polls/{pollId}")
@CrossOrigin // temporary, we’ll tighten later
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/vote")
    public VoteResponse vote(@PathVariable UUID pollId, @Valid @RequestBody VoteRequest request) {
        return voteService.submitVote(pollId, request);
    }

    @GetMapping("/results")
    public PollResultsResponse results(@PathVariable UUID pollId) {
        return voteService.getResults(pollId);
    }
}
