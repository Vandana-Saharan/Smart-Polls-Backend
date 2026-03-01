package com.smartpolls.smartpollsapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vote")
@Getter
@NoArgsConstructor
public class Vote {

    @Id
    private UUID id;

    @Column(name = "poll_id", nullable = false)
    private UUID pollId;

    @Column(name = "option_id", nullable = false)
    private UUID optionId;

    @Column(name = "voter_id", nullable = false)
    private String voterId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Vote(UUID id, UUID pollId, UUID optionId, String voterId, Instant createdAt) {
        this.id = id;
        this.pollId = pollId;
        this.optionId = optionId;
        this.voterId = voterId;
        this.createdAt = createdAt;
    }
}
