package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.Message;

import java.util.List;

public interface MessageRepo extends Repository<Long, Message>{
    public List<Long> getReceiversIds(Long id);
    public Long getSenderId(Long id);
    public List<Message> findAllInChat(Long userId1, Long userId2);
}

