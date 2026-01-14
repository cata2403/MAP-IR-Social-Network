package com.ubb.observer;

public enum ChangeEvent {
    USERS_UPDATE,
    FRIENDSHIPS_UPDATE,
    MESSAGES_UPDATE,
    FRIEND_REQUEST,
    EVENTS_UPDATE,
    EVENT_ENDED;
    private Long receiverId;
    private Double score;
    public Long getReceiverId() {
        return receiverId;
    }
    public ChangeEvent setReceiverId(Long id) {
        this.receiverId = id;
        return this;
    }

    public Double getScore() {
        return score;
    }

    public ChangeEvent setScore(Double score) {
        this.score = score;
        return this;
    }
}

