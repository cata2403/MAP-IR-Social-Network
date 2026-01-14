package com.ubb.domain_layer.entities;

import com.ubb.domain_layer.enums.FriendRequest;
import com.ubb.exceptions.DomainException;

public class Friendship extends Entity<Long>{

    private final Long idUser1;
    private final Long idUser2;
    private FriendRequest status;

    /**
     * Creates an instance of a friendship entity
     * A friendship is a relation between two users
     * @param id friendship id
     * @param idUser1 id of first user
     * @param idUser2 id of second user
     * @param status status of the friendship
     * @throws DomainException throws exception if user's ids are the same
     */
    public Friendship(Long id, Long idUser1, Long idUser2,  FriendRequest status) throws DomainException {

        super(id);

        if(idUser1.equals(idUser2)){
            throw new DomainException("A user cannot befriend itself");
        }

        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.status = status;
    }

    /**
     * Return the id of the first user
     * @return first user's id
     */
    public Long getIdUser1() {
        return idUser1;
    }

    /**
     * Returns the id of the second user
     * @return second user's id
     */
    public Long getIdUser2() {
        return idUser2;
    }

    /**
     * Returns the status of the friendship
     * @return friendship's status
     */
    public FriendRequest getStatus() {
        return status;
    }

    /**
     * Changes the status of the friendship
     * @param status the new status, status can be from {}
     */
    public void setStatus(FriendRequest status) {
        this.status = status;
    }
}

