package com.smartpolls.smartpollsapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "poll")
@Getter
@NoArgsConstructor
public class Poll {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String question;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(
            mappedBy = "poll",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PollOption> options;

    public Poll(UUID id, String question, Instant createdAt, List<PollOption> options) {
        this.id = id;
        this.question = question;
        this.createdAt = createdAt;
        this.options = options;
    }

}
