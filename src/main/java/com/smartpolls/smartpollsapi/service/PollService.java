package com.smartpolls.smartpollsapi.service;

import com.smartpolls.smartpollsapi.dto.CreatePollRequest;
import com.smartpolls.smartpollsapi.dto.PollOptionResponse;
import com.smartpolls.smartpollsapi.dto.PollResponse;
import com.smartpolls.smartpollsapi.entity.Poll;
import com.smartpolls.smartpollsapi.entity.PollOption;
import com.smartpolls.smartpollsapi.exception.PollOwnershipException;
import com.smartpolls.smartpollsapi.repository.PollRepository;
import com.smartpolls.smartpollsapi.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;

    @Transactional
    public PollResponse createPoll(CreatePollRequest request, JwtUserPrincipal principal) {
        UUID pollId = UUID.randomUUID();

        Poll poll = new Poll(
                pollId,
                request.question(),
                Instant.now(),
                null,
                principal.userId()
        );

        List<PollOption> options = request.options().stream()
                .map(text -> new PollOption(UUID.randomUUID(), poll, text))
                .toList();

        Poll fullPoll = new Poll(
                poll.getId(),
                poll.getQuestion(),
                poll.getCreatedAt(),
                options,
                poll.getOwnerId()
        );

        Poll saved = pollRepository.save(fullPoll);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PollResponse> listPolls() {
        return pollRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PollResponse getPoll(UUID pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new NoSuchElementException("Poll not found"));
        return toResponse(poll);
    }

    @Transactional
    public void deletePoll(UUID pollId, JwtUserPrincipal principal) {
        // Will throw if not found – surfaces as 500; controller can handle if needed
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new NoSuchElementException("Poll not found"));

        boolean isOwner = poll.getOwnerId() != null && poll.getOwnerId().equals(principal.userId());
        if (!isOwner && !principal.isAdmin()) {
            throw new PollOwnershipException();
        }

        pollRepository.deleteById(pollId);
    }

    private PollResponse toResponse(Poll poll) {
        return new PollResponse(
                poll.getId(),
                poll.getQuestion(),
                poll.getCreatedAt(),
                poll.getOwnerId(),
                poll.getOwnerId(),
                poll.getOptions().stream()
                        .map(o -> new PollOptionResponse(o.getId(), o.getText()))
                        .toList()
        );
    }
}
