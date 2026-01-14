package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.Person;
import com.ubb.exceptions.RepoException;
import com.ubb.infrastructure_layer.utils.DateTimeFormats;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBPersonRepo implements PersonRepo{

    private final String url;
    private final String username;
    private final String password;

    public DBPersonRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Person entity) throws RepoException {

        if ( entity == null ) {
            throw new RepoException("<<Person entity is null>>");
        }

        String sqlInsert1 = "INSERT INTO person " +
                "(pid, first_name, last_name, occupation, birth_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        String sqlInsert2 = "INSERT INTO app_user " +
                "(uid, username, pass, email, did, pid) " +
                "VALUES (?, ?, ?, ?, NULL, ?)";

        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            connection.setAutoCommit(false);
            try(PreparedStatement statement1 = connection.prepareStatement(sqlInsert1);
                PreparedStatement statement2 = connection.prepareStatement(sqlInsert2); ) {

                statement1.setLong(1, entity.getId());
                statement1.setString(2, entity.getFirstName());
                statement1.setString(3, entity.getLastName());
                statement1.setString(4, entity.getOccupation());
                statement1.setDate(5, java.sql.Date.valueOf(
                        entity.getDateOfBirth())
                );

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
    public Optional<Person> delete(Long id) throws RepoException {

        if ( id == null ){
            throw new RepoException("<<id is null>>\n");
        }

        String sqlDelete0 = "DELETE FROM event_subscription WHERE uid = ?";
        String sqlDelete1 = "DELETE FROM app_user WHERE uid = ?";
        String sqlDelete2 = "DELETE FROM person WHERE pid = ?";

        try( Connection connect = DriverManager.getConnection(url, username, password) ){

            connect.setAutoCommit(false);
            try( PreparedStatement statement0 = connect.prepareStatement(sqlDelete0);
                 PreparedStatement statement1 = connect.prepareStatement(sqlDelete1);
                 PreparedStatement statement2 = connect.prepareStatement(sqlDelete2); ) {

                statement0.setLong(1, id);
                statement1.setLong(1, id);
                statement2.setLong(1, id);

                Optional<Person> person = get(id);
                statement0.executeUpdate();
                statement1.executeUpdate();
                statement2.executeUpdate();

                connect.commit();
                return person;
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
    public void update(Person entity) throws RepoException {

        if ( entity == null ){
            throw new RepoException("<<entity is null>>\n");
        }

        String sqlUpdate1 = "UPDATE person SET " +
                "first_name = ?, last_name = ?, " +
                "occupation = ?, birth_date = ? WHERE pid = ?";

        String sqlUpdate2 = "UPDATE app_user SET " +
                "username = ?, pass = ?, email = ? " +
                "WHERE uid = ?";

        try( Connection connection = DriverManager.getConnection( url, username, password )){

            connection.setAutoCommit(false);
            try( PreparedStatement statement1 = connection.prepareStatement(sqlUpdate1);
                 PreparedStatement statement2 = connection.prepareStatement(sqlUpdate2); ) {

                statement1.setString(1, entity.getFirstName());
                statement1.setString(2, entity.getLastName());
                statement1.setString(3, entity.getOccupation());
                statement1.setDate(4, java.sql.Date.valueOf(entity.getDateOfBirth()));
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
    public Optional<Person> get(Long id) throws RepoException {

        if ( id == null ){
            throw new RepoException("<<id is null>>\n");
        }

        String sqlGet = "SELECT * FROM app_user AS au JOIN person AS p ON au.pid = p.pid WHERE au.uid = ?;";
        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if ( !resultSet.next() ){
                throw new RepoException("<<Entity with id does not exist>>\n");
            }

            Person person = new Person(
                    resultSet.getLong("uid"),
                    resultSet.getString("username"),
                    resultSet.getString("pass"),
                    resultSet.getString("email")
            );

            person.setFirstName( resultSet.getString("first_name") )
                    .setLastName( resultSet.getString("last_name") )
                    .setOccupation( resultSet.getString("occupation") )
                    .setDateOfBirth( LocalDate.parse(
                            resultSet.getString("birth_date") ,
                            DateTimeFormats.getDateFormatter2()
                    ));

            return Optional.of(person);

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Iterable<Person> getAll() {

        String sqlGet = "SELECT * FROM app_user AS au JOIN person AS p ON au.pid = p.pid";
        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Person> persons = new ArrayList<>();
            while( resultSet.next() ){

                Person person = new Person(
                        resultSet.getLong("uid"),
                        resultSet.getString("username"),
                        resultSet.getString("pass"),
                        resultSet.getString("email")
                );

                person.setFirstName( resultSet.getString("first_name") )
                        .setLastName( resultSet.getString("last_name") )
                        .setOccupation( resultSet.getString("occupation") )
                        .setDateOfBirth( LocalDate.parse(
                                resultSet.getString("birth_date") ,
                                DateTimeFormats.getDateFormatter2()
                        ));

                persons.add(person);

            }

            return persons;

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Optional<Person> findByUsername(String user) {

        String sqlGet = "SELECT * FROM app_user AS au JOIN person AS p ON au.pid = p.pid WHERE au.username = ?;";
        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();

            if ( !resultSet.next() ){
                return Optional.empty();
            }

            Person person = new Person(
                    resultSet.getLong("uid"),
                    resultSet.getString("username"),
                    resultSet.getString("pass"),
                    resultSet.getString("email")
            );

            person.setFirstName( resultSet.getString("first_name") )
                    .setLastName( resultSet.getString("last_name") )
                    .setOccupation( resultSet.getString("occupation") )
                    .setDateOfBirth( LocalDate.parse(
                            resultSet.getString("birth_date") ,
                            DateTimeFormats.getDateFormatter2()
                    ));

            return Optional.of(person);

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }
}

