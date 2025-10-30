package com.ubb.business_logic.dtos;

public class FriendshipDTO {
    final private Long user1;
    final private Long user2;
    public FriendshipDTO(Long user1, Long user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
    public Long getUser1() {
        return user1;
    }
    public Long getUser2() {
        return user2;
    }
}
