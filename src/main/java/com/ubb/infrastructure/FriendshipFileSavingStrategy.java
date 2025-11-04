package com.ubb.infrastructure;
import com.ubb.domain.entity_types.FriendRequest;
import com.ubb.domain.entities.Friendship;

public class FriendshipFileSavingStrategy implements DataTransferStrategy<Long, Friendship> {

    @Override
    public String serialize(Friendship friendship) {

        return  String.join(",",
                String.valueOf(friendship.getId()),
                String.valueOf(friendship.getIdUser1()),
                String.valueOf(friendship.getIdUser2()),
                friendship.getStatus().name());
    }

    @Override
    public Friendship deserialize(String fileLine) {

        String[] friendshipData = fileLine.split(",");

        return new Friendship( Long.parseLong(friendshipData[0]),
                               Long.parseLong(friendshipData[1]),
                               Long.parseLong(friendshipData[2]),
                               FriendRequest.valueOf(friendshipData[3]) );
    }
}
