CREATE TABLE vote (
    id UUID PRIMARY KEY,
    poll_id UUID NOT NULL,
    option_id UUID NOT NULL,
    voter_id TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_vote_poll
        FOREIGN KEY (poll_id)
        REFERENCES poll (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_vote_option
        FOREIGN KEY (option_id)
        REFERENCES poll_option (id)
        ON DELETE CASCADE
);

-- Prevent duplicate vote per poll per voter/device
CREATE UNIQUE INDEX ux_vote_poll_voter ON vote (poll_id, voter_id);

-- Helpful index for results aggregation
CREATE INDEX ix_vote_poll ON vote (poll_id);
