package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.Swimmers;
import com.ubb.exceptions.RepoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBFlockRepo implements FlockRepo {

    private String url;
    private String username;
    private String password;

    public DBFlockRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Swimmers entity) throws RepoException {

        if ( entity ==  null ) {
            throw new RepoException("<<entity is null>>");
        }

        String sqlInsert = "INSERT INTO flock(fId,flock_name) VALUES (?,?)";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            PreparedStatement statement = conn.prepareStatement( sqlInsert );
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFlockName());

            statement.executeUpdate();

        }
        catch (SQLException error) {
            throw new RepoException(error.getMessage());
        }

    }

    @Override
    public Optional<Swimmers> delete(Long id) throws RepoException {
        if ( id == null ) {
            throw new RepoException("<<id is null>>");
        }

        String sqlDelete = "DELETE FROM flock WHERE fId = ?";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement statement = connection.prepareStatement( sqlDelete );
            statement.setLong(1, id);

            Swimmers entity = get(id).get();
            statement.executeUpdate();

            return Optional.of(entity);

        }
        catch (SQLException error) {
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public void update(Swimmers entity) throws RepoException {

        if ( entity ==  null ) {
            throw new RepoException("<<entity is null>>");
        }

        String  sqlUpdate = "UPDATE flock SET flock_name=? WHERE fId=?";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement statement = connection.prepareStatement( sqlUpdate );
            statement.setString(1,entity.getFlockName());
            statement.setLong(2,entity.getId());

            statement.executeUpdate();

        }
        catch (SQLException error) {
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Optional<Swimmers> get(Long id) throws RepoException {

        if ( id == null ) {
            throw new RepoException("<<id is null>>");
        }

        String sqlGet = "SELECT * FROM flock WHERE fId = ?";
        try(Connection connection = DriverManager.getConnection( url, username, password )){

            PreparedStatement statement = connection.prepareStatement( sqlGet );
            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            if(!rs.next()) {
                throw new RepoException("<<flock does not exist>>");
            }

            Swimmers swimmer = new Swimmers(
                    rs.getLong("fId"), rs.getString("flock_name")
            );
            return Optional.of(swimmer);
        }
        catch (SQLException error) {
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Iterable<Swimmers> getAll() {

        String sqlGetAll = "SELECT * FROM flock";
        try( Connection connection = DriverManager.getConnection( url, username, password )) {

            PreparedStatement statement = connection.prepareStatement( sqlGetAll );
            ResultSet rs = statement.executeQuery();

            List<Swimmers> list = new ArrayList<>();
            while(rs.next()) {

                list.add(new Swimmers(
                        rs.getLong("fId"), rs.getString("flock_name")
                ));

            }

            return list;
        }
        catch (SQLException error) {
            throw new RepoException(error.getMessage());
        }
    }

}

