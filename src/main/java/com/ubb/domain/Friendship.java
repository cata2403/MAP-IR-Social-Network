package com.ubb.domain;

public class Friendship extends Entity<Long>{

    private final Long idUser1;
    private final Long idUser2;
    private FriendRequest status;

    public Friendship(Long id, Long idUser1, Long idUser2,  FriendRequest status) {
        super(id);
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.status = status;
    }

    public Long getIdUser1() {
        return idUser1;
    }

    public Long getIdUser2() {
        return idUser2;
    }

    public FriendRequest getStatus() {
        return status;
    }

    public void setStatus(FriendRequest status) {
        this.status = status;
    }
}
