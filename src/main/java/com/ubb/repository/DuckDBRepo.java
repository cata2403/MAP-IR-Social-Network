package com.ubb.repository;

import com.ubb.business_logic.dtos.DuckExtrasDTO;
import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.domain.UserFactory;
import com.ubb.domain.entities.Duck;
import com.ubb.domain.entity_types.DuckType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DuckDBRepo implements Repository<Long, Duck>{

    private final String url;
    private final String username;
    private final String password;

    public DuckDBRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public void add(Duck entity) throws RepoException {

        String sqlInsert = "INSERT INTO duck " +
                "(did, username, pass, email, duck_type, speed, res, flockId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            preparedStatement.setLong( 1, entity.getId() );
            preparedStatement.setString( 2, entity.getUsername() );
            preparedStatement.setString( 3, entity.getPassword() );
            preparedStatement.setString( 4, entity.getEmail() );
            preparedStatement.setString( 5, entity.getDuckType().toString() );
            preparedStatement.setDouble( 6, entity.getSpeed() );
            preparedStatement.setDouble( 7, entity.getResistance() );
            preparedStatement.setLong( 8, entity.getFlockId() );

            preparedStatement.executeUpdate();

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }

    }

    @Override
    public Duck delete(Long id) throws RepoException {

        if ( id == null ){
            throw new RepoException("<<id is null>>\n");
        }

        String sqlDelete = "DELETE FROM duck WHERE did = ?";
        try( Connection connect = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connect.prepareStatement(sqlDelete);
            preparedStatement.setLong(1, id);

            Duck duck = get(id);
            preparedStatement.executeUpdate();

            return duck;
        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public void update(Duck entity) throws RepoException {
        if ( entity == null ){
            throw new RepoException("<<entity is null>>\n");
        }

        String sqlUpdate = "UPDATE duck SET " +
                "did = ?, username = ?, pass = ?, email = ?, " +
                "duck_type = ?, speed = ?, res = ?, flockId = ? " +
                "WHERE did = ?";

        try( Connection connection = DriverManager.getConnection( url, username, password )){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setString(5, entity.getDuckType().toString() );
            preparedStatement.setDouble(6, entity.getSpeed());
            preparedStatement.setDouble(7, entity.getResistance());
            preparedStatement.setLong(8, entity.getFlockId());

            preparedStatement.setLong( 9, entity.getId() );
            preparedStatement.executeUpdate();

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Duck get(Long id) throws RepoException {

        if ( id == null ){
            throw new RepoException("<<id is null>>\n");
        }

        String sqlGet = "SELECT * FROM duck WHERE did = ?";

        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if ( !resultSet.next() ){
                throw new RepoException("<<Entity with id does not exist>>\n");
            }

            FullUserInfoDTO dto1 = new FullUserInfoDTO(
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("pass")
            );
            DuckExtrasDTO dto2 = new DuckExtrasDTO(
                    resultSet.getLong("speed"),
                    resultSet.getLong("res"),
                    DuckType.valueOf(resultSet.getString("duck_type"))
            );

            Duck duck = (Duck) UserFactory.createUser( dto1, dto2, resultSet.getLong("did") );
            duck.setFlockId( resultSet.getLong("flockId") );

            return duck;
        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public List<Duck> getAll() {

        String sqlGet = "SELECT * FROM duck";
        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Duck> ducks = new ArrayList<>();
            while( resultSet.next() ){

                FullUserInfoDTO dto1 = new FullUserInfoDTO(
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("pass")
                );
                DuckExtrasDTO dto2 = new DuckExtrasDTO(
                        resultSet.getLong("speed"),
                        resultSet.getLong("res"),
                        DuckType.valueOf(resultSet.getString("duck_type"))
                );

                Duck duck = (Duck) UserFactory.createUser( dto1, dto2, resultSet.getLong("did") );
                duck.setFlockId( resultSet.getLong("flockId") );

                ducks.add(duck);

            }

            return ducks;

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }
}
