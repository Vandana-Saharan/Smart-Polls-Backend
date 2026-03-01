CREATE TABLE poll (
    id UUID PRIMARY KEY,
    question TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE poll_option (
    id UUID PRIMARY KEY,
    poll_id UUID NOT NULL,
    text TEXT NOT NULL,
    CONSTRAINT fk_poll_option_poll
        FOREIGN KEY (poll_id)
        REFERENCES poll (id)
        ON DELETE CASCADE
);
