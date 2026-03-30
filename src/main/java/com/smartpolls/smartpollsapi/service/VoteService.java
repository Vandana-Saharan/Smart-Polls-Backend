package com.smartpolls.smartpollsapi.service;

import com.smartpolls.smartpollsapi.dto.PollResultsResponse;
import com.smartpolls.smartpollsapi.dto.VoteResponse;
import com.smartpolls.smartpollsapi.entity.Poll;
import com.smartpolls.smartpollsapi.entity.PollOption;
import com.smartpolls.smartpollsapi.entity.Vote;
import com.smartpolls.smartpollsapi.repository.PollRepository;
import com.smartpolls.smartpollsapi.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public VoteResponse submitVote(UUID pollId, UUID optionId, String voterId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new NoSuchElementException("Poll not found"));

        // Ensure option belongs to poll
        boolean validOption = poll.getOptions().stream()
                .map(PollOption::getId)
                .anyMatch(id -> id.equals(optionId));

        if (!validOption) {
            return new VoteResponse(false, "INVALID_OPTION");
        }

        // Fast pre-check (helps UX, but uniqueness index is final authority)
        if (voteRepository.existsByPollIdAndVoterId(pollId, voterId)) {
            return new VoteResponse(false, "ALREADY_VOTED");
        }

        try {
            Vote vote = new Vote(
                    UUID.randomUUID(),
                    pollId,
                    optionId,
                    voterId,
                    Instant.now()
            );

            voteRepository.save(vote);
            return new VoteResponse(true, "VOTED");
        } catch (DataIntegrityViolationException ex) {
            // In case two requests race, unique index wins
            return new VoteResponse(false, "ALREADY_VOTED");
        }
    }

    @Transactional(readOnly = true)
    public PollResultsResponse getResults(UUID pollId) {
        // Ensure poll exists
        pollRepository.findById(pollId)
                .orElseThrow(() -> new NoSuchElementException("Poll not found"));

        Map<UUID, Long> votes = new HashMap<>();
        for (Object[] row : voteRepository.countVotesByOption(pollId)) {
            UUID optionId = (UUID) row[0];
            Long count = (Long) row[1];
            votes.put(optionId, count);
        }

        return new PollResultsResponse(pollId, votes);
    }
}
