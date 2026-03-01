package com.smartpolls.smartpollsapi.service;

import com.smartpolls.smartpollsapi.dto.CreatePollRequest;
import com.smartpolls.smartpollsapi.dto.PollOptionResponse;
import com.smartpolls.smartpollsapi.dto.PollResponse;
import com.smartpolls.smartpollsapi.entity.Poll;
import com.smartpolls.smartpollsapi.entity.PollOption;
import com.smartpolls.smartpollsapi.repository.PollRepository;
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
    public PollResponse createPoll(CreatePollRequest request) {
        UUID pollId = UUID.randomUUID();

        Poll poll = new Poll(
                pollId,
                request.question(),
                Instant.now(),
                null
        );

        List<PollOption> options = request.options().stream()
                .map(text -> new PollOption(UUID.randomUUID(), poll, text))
                .toList();

        Poll fullPoll = new Poll(
                poll.getId(),
                poll.getQuestion(),
                poll.getCreatedAt(),
                options
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
                .orElseThrow(() -> new NoSuchElementException("POLL_NOT_FOUND"));
        return toResponse(poll);
    }

    @Transactional
    public void deletePoll(UUID pollId) {
        // Will throw if not found – surfaces as 500; controller can handle if needed
        if (!pollRepository.existsById(pollId)) {
            throw new NoSuchElementException("POLL_NOT_FOUND");
        }
        pollRepository.deleteById(pollId);
    }

    private PollResponse toResponse(Poll poll) {
        return new PollResponse(
                poll.getId(),
                poll.getQuestion(),
                poll.getCreatedAt(),
                poll.getOptions().stream()
                        .map(o -> new PollOptionResponse(o.getId(), o.getText()))
                        .toList()
        );
    }
}
