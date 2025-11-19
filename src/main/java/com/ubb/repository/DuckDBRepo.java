package com.ubb.repository;

import com.ubb.business_logic.dtos.DuckExtrasDTO;
import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.domain.UserFactory;
import com.ubb.domain.entities.Duck;
import com.ubb.domain.entity_types.DuckType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

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

        String sqlInsert1 = "INSERT INTO duck " +
                "(did, duck_type, speed, res, flockId) " +
                "VALUES (?, ?, ?, ?, NULL)";

        String sqlInsert2 = "INSERT INTO app_user " +
                "(uid, username, pass, email, did, pid) " +
                "VALUES (?, ?, ?, ?, ?, NULL)";

        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            connection.setAutoCommit(false);
            try( PreparedStatement statement1 = connection.prepareStatement(sqlInsert1);
                 PreparedStatement statement2 = connection.prepareStatement(sqlInsert2); ) {

                statement1.setLong(1, entity.getId());
                statement1.setString(2, entity.getDuckType().toString());
                statement1.setDouble(3, entity.getSpeed());
                statement1.setDouble(4, entity.getResistance());

                statement2.setLong(1, entity.getId());
                statement2.setString(2, entity.getUsername());
                statement2.setString(3, entity.getPassword());
                statement2.setString(4, entity.getEmail());
                statement2.setLong(5, entity.getId());

                statement1.executeUpdate();
                statement2.executeUpdate();

                connection.commit();
            }
            catch ( SQLException error ) {
                connection.rollback();
                throw error;
            }

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

        String sqlDelete1 = "DELETE FROM app_user WHERE uid = ?";
        String sqlDelete2 = "DELETE FROM duck WHERE did = ?";

        try( Connection connect = DriverManager.getConnection(url, username, password) ){

            connect.setAutoCommit(false);
            try( PreparedStatement statement1 = connect.prepareStatement(sqlDelete1);
                 PreparedStatement statement2 = connect.prepareStatement(sqlDelete2); ) {

                statement1.setLong(1, id);
                statement2.setLong(1, id);

                Duck duck = get(id);
                statement1.executeUpdate();
                statement2.executeUpdate();

                connect.commit();

                return duck;
            }
            catch ( SQLException error ) {
                connect.rollback();
                throw error;
            }

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

        String sqlUpdate1 = "UPDATE duck SET " +
                "duck_type = ?, speed = ?, res = ?, flockId = ? " +
                "WHERE did = ?";

        String sqlUpdate2 = "UPDATE app_user SET " +
                "username = ?, pass = ?, email = ? " +
                "WHERE uid = ?";

        try( Connection connection = DriverManager.getConnection( url, username, password )){

            connection.setAutoCommit(false);
            try ( PreparedStatement statement1 = connection.prepareStatement(sqlUpdate1);
                  PreparedStatement statement2 = connection.prepareStatement(sqlUpdate2); ) {

                statement1.setString(1, entity.getDuckType().toString());
                statement1.setDouble(2, entity.getSpeed());
                statement1.setDouble(3, entity.getResistance());

                if(entity.getFlockId() == -1)
                    statement1.setNull(4, NULL);
                else statement1.setLong(4, entity.getFlockId());
                statement1.setLong(5, entity.getId());

                statement2.setString(1, entity.getUsername());
                statement2.setString(2, entity.getPassword());
                statement2.setString(3, entity.getEmail());
                statement2.setLong(4, entity.getId());

                statement1.executeUpdate();
                statement2.executeUpdate();

                connection.commit();
            }
            catch ( SQLException error ) {
                connection.rollback();
                throw error;
            }

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

        String sqlGet = "SELECT * FROM app_user AS au JOIN duck AS d ON au.did = d.did WHERE au.uid = ?";

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

            Duck duck = (Duck) UserFactory.createUser( dto1, dto2, resultSet.getLong("uid") );
            duck.setFlockId( resultSet.getLong("flockId") );

            return duck;
        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public List<Duck> getAll() {

        String sqlGet = "SELECT * FROM app_user AS au JOIN duck AS d ON au.did = d.did";
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

                Duck duck = (Duck) UserFactory.createUser( dto1, dto2, resultSet.getLong("uid") );
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
