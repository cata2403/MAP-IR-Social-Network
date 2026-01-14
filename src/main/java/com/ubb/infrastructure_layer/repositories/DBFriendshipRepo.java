package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.Friendship;
import com.ubb.domain_layer.enums.FriendRequest;
import com.ubb.exceptions.RepoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBFriendshipRepo implements FriendshipRepo {

    private String url;
    private String username;
    private String password;

    public DBFriendshipRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Friendship entity) throws RepoException {

        if ( entity == null ) {
            throw new RepoException("entity is null");
        }

        String sqlInsert = "INSERT INTO friendship(fid, uid1, uid2, status) VALUES (?, ?, ?, ?)";
        try( Connection conn = DriverManager.getConnection(url, username, password) ) {

            PreparedStatement stmt = conn.prepareStatement(sqlInsert);
            stmt.setLong(1, entity.getId());
            stmt.setLong(2, entity.getIdUser1());
            stmt.setLong(3, entity.getIdUser2());
            stmt.setString(4, entity.getStatus().toString());

            stmt.executeUpdate();

        }
        catch ( SQLException error ) {
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Optional<Friendship> delete(Long id) throws RepoException {

        if ( id == null ) {
            throw new RepoException("id is null");
        }

        String sqlDelete = "DELETE FROM friendship WHERE fid = ?";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement statement = connection.prepareStatement(sqlDelete);
            statement.setLong(1, id);

            Optional<Friendship> friendship = get(id);
            statement.executeUpdate();

            return friendship;

        }
        catch ( SQLException error ) {
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public void update(Friendship entity) throws RepoException {

        if ( entity == null ) {
            throw new RepoException("entity is null");
        }

        String sqlUpdate = "UPDATE friendship SET  uid1 = ?, uid2 = ?, status = ? WHERE fid = ?";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement statement = connection.prepareStatement(sqlUpdate);
            statement.setLong(1, entity.getIdUser1());
            statement.setLong(2, entity.getIdUser2());
            statement.setString(3, entity.getStatus().toString());
            statement.setLong(4, entity.getId());

            statement.executeUpdate();

        }
        catch ( SQLException error ) {
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Optional<Friendship> get(Long id) throws RepoException {


        if ( id == null ) {
            throw new RepoException("id is null");
        }

        String sqlSelect = "SELECT * FROM friendship WHERE fid = ?";
        try ( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement stmt = connection.prepareStatement(sqlSelect);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if ( !rs.next() )
                throw new RepoException("id not found");

            Friendship friendship = new Friendship(
                    rs.getLong("fid"), rs.getLong("uid1"),
                    rs.getLong("uid2"), FriendRequest.valueOf(rs.getString("status"))
            );

            return Optional.of(friendship);

        }
        catch ( SQLException error ) {
            throw new RepoException(error.getMessage());
        }

    }

    @Override
    public Iterable<Friendship> getAll() {

        String sqlSelect = "SELECT * FROM friendship";
        try ( Connection connection = DriverManager.getConnection( url, username, password ) ) {
            PreparedStatement statement = connection.prepareStatement(sqlSelect);
            ResultSet rs = statement.executeQuery();

            List<Friendship> friendships = new ArrayList<>();

            while ( rs.next() ) {
                Friendship friendship = new Friendship(
                        rs.getLong("fid"), rs.getLong("uid1"),
                        rs.getLong("uid2"), FriendRequest.valueOf(rs.getString("status"))
                );
                friendships.add(friendship);
            }

            return friendships;
        }
        catch ( SQLException error ) {
            throw new RepoException(error.getMessage());
        }
    }
}

