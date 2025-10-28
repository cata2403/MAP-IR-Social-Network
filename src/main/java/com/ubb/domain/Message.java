package com.ubb.domain;

import java.time.LocalDateTime;

public class Message<ID> extends Entity<ID>{
    private final ID idSender;
    private final ID idReceiver;
    private final String message;
    private final LocalDateTime timestamp;

    public Message(ID id, ID sender, ID receiver, String message) {
        super(id);
        this.idSender = sender;
        this.idReceiver = receiver;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    public ID getIdSender() {
        return idSender;
    }
    public ID getIdReceiver() {
        return idReceiver;
    }
    public String getMessage() {
        return message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
