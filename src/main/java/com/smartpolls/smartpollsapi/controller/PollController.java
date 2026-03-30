package com.smartpolls.smartpollsapi.controller;

import com.smartpolls.smartpollsapi.dto.CreatePollRequest;
import com.smartpolls.smartpollsapi.dto.MessageResponse;
import com.smartpolls.smartpollsapi.dto.PollResponse;
import com.smartpolls.smartpollsapi.exception.UnauthorizedException;
import com.smartpolls.smartpollsapi.security.JwtUserPrincipal;
import com.smartpolls.smartpollsapi.service.PollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<PollResponse> createPoll(
            @Valid @RequestBody CreatePollRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pollService.createPoll(request, requirePrincipal(authentication)));
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
    public MessageResponse deletePoll(@PathVariable UUID pollId, Authentication authentication) {
        pollService.deletePoll(pollId, requirePrincipal(authentication));
        return new MessageResponse("Poll deleted successfully");
    }

    private JwtUserPrincipal requirePrincipal(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUserPrincipal principal)) {
            throw new UnauthorizedException();
        }
        return principal;
    }
}
