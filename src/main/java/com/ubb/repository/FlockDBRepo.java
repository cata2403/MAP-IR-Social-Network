package com.ubb.repository;

import com.ubb.domain.entities.SwimMasters;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlockDBRepo implements Repository<Long, SwimMasters> {

    String url;
    String username;
    String password;

    public FlockDBRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public void add(SwimMasters entity) throws RepoException {

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
    public SwimMasters delete(Long id) throws RepoException {
        if ( id == null ) {
            throw new RepoException("<<id is null>>");
        }

        String sqlDelete = "DELETE FROM flock WHERE fId = ?";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement statement = connection.prepareStatement( sqlDelete );
            statement.setLong(1, id);

            SwimMasters entity = get(id);
            statement.executeUpdate();

            return entity;

        }
        catch (SQLException error) {
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public void update(SwimMasters entity) throws RepoException {
        
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
    public SwimMasters get(Long id) throws RepoException {

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

            return new SwimMasters(
                    rs.getLong("fId"), rs.getString("flock_name")
            );
        }
        catch (SQLException error) {
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public List<SwimMasters> getAll() {

        String sqlGetAll = "SELECT * FROM flock";
        try( Connection connection = DriverManager.getConnection( url, username, password )) {

            PreparedStatement statement = connection.prepareStatement( sqlGetAll );
            ResultSet rs = statement.executeQuery();

            List<SwimMasters> list = new ArrayList<>();
            while(rs.next()) {

                list.add(new SwimMasters(
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
