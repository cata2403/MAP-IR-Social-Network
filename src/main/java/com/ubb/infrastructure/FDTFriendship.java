package com.ubb.infrastructure;

import com.ubb.domain.Entity;
import com.ubb.domain.FriendRequest;
import com.ubb.domain.Friendship;

public class FDTFriendship implements FileDataTransfer<Long>{

    @Override
    public String serialization(Entity<Long> o) {
        return ((Friendship)o).getId().toString() + ',' + ((Friendship) o).getIdUser1().toString() + ',' + ((Friendship) o).getIdUser2().toString() + ((Friendship) o).getStatus().name();
    }

    @Override
    public Entity<Long> deserialization(String s) {
        String[] split = s.split(",");
        return new Friendship(Long.parseLong(split[0]),Long.parseLong(split[1]),Long.parseLong(split[2]), FriendRequest.valueOf(split[3]));
    }
}
