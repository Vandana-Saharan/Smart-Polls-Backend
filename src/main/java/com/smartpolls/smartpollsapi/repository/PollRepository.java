package com.smartpolls.smartpollsapi.repository;

import com.smartpolls.smartpollsapi.entity.Poll;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PollRepository extends JpaRepository<Poll, UUID> {

    @EntityGraph(attributePaths = "options")
    List<Poll> findAll();

    @EntityGraph(attributePaths = "options")
    Optional<Poll> findById(UUID id);
}
