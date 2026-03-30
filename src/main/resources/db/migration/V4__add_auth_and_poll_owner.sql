-- Users table for JWT auth
CREATE TABLE app_user (
    id UUID PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Poll ownership: which authenticated user can delete the poll
ALTER TABLE poll
ADD COLUMN owner_id UUID;

ALTER TABLE poll
ADD CONSTRAINT fk_poll_owner
FOREIGN KEY (owner_id) REFERENCES app_user (id)
ON DELETE SET NULL;

CREATE INDEX ix_poll_owner_id ON poll(owner_id);

