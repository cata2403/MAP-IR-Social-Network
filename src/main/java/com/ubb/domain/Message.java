package com.ubb.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private final Long idSender;
    private final Long idReceiver;
    private final String message;
    private final LocalDateTime timestamp;

    public Message(Long id, Long sender, Long receiver, String message) {
        super(id);
        this.idSender = sender;
        this.idReceiver = receiver;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    public Long getIdSender() {
        return idSender;
    }
    public Long getIdReceiver() {
        return idReceiver;
    }
    public String getMessage() {
        return message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
