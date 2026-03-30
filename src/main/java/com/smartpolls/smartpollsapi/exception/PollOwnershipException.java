package com.smartpolls.smartpollsapi.exception;

public class PollOwnershipException extends RuntimeException {
    public PollOwnershipException() {
        super("You can only delete your own polls");
    }
}
