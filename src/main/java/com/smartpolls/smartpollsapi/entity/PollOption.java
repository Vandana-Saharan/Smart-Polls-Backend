package com.smartpolls.smartpollsapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "poll_option")
@Getter
@NoArgsConstructor
public class PollOption {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(nullable = false)
    private String text;

    public PollOption(UUID id, Poll poll, String text) {
        this.id = id;
        this.poll = poll;
        this.text = text;
    }
}
