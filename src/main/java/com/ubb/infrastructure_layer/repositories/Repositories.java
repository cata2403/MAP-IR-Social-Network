package com.ubb.infrastructure_layer.repositories;
import com.ubb.domain_layer.entities.Duck;

public record Repositories(
        PersonRepo personRepository,
        DuckRepo duckRepository,
        FriendshipRepo friendshipRepository,
        MessageRepo messageRepo,
        EventRepo eventRepo
) {}

