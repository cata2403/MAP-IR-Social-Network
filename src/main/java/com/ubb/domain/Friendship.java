package com.ubb.domain;

public class Friendship<ID> extends Entity<ID>{
    private final ID idUser1;
    private final ID idUser2;
    private FriendRequest status;
    public Friendship(ID id, ID idUser1, ID idUser2,  FriendRequest status) {
        super(id);
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.status = status;
    }
    public ID getIdUser1() {
        return idUser1;
    }
    public ID getIdUser2() {
        return idUser2;
    }
    public FriendRequest getStatus() {
        return status;
    }
    public void setStatus(FriendRequest status) {
        this.status = status;
    }
}
