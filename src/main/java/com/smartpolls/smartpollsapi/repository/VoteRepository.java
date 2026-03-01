package com.smartpolls.smartpollsapi.repository;

import com.smartpolls.smartpollsapi.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

    boolean existsByPollIdAndVoterId(UUID pollId, String voterId);

    @Query("""
        select v.optionId, count(v.id)
        from Vote v
        where v.pollId = :pollId
        group by v.optionId
    """)
    List<Object[]> countVotesByOption(UUID pollId);
}
