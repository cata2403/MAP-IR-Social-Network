package com.ubb.repository;

import com.ubb.domain.entities.Person;
import com.ubb.utils.DateTimeFormats;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonDBRepo implements Repository<Long, Person>{

    private final String url;
    private final String username;
    private final String password;

    public PersonDBRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Person entity) throws RepoException {

        String sqlInsert = "INSERT INTO person " +
                           "(pid, username, pass, email, first_name, last_name, occupation, birth_date) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setString(5, entity.getFirstName());
            preparedStatement.setString(6, entity.getLastName());
            preparedStatement.setString(7, entity.getOccupation());
            preparedStatement.setDate(8, java.sql.Date.valueOf(entity.getDateOfBirth())
            );

            preparedStatement.executeUpdate();

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Person delete(Long id) throws RepoException {

        if ( id == null ){
            throw new RepoException("<<id is null>>\n");
        }

        String sqlDelete = "DELETE FROM person WHERE pid = ?";
        try( Connection connect = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connect.prepareStatement(sqlDelete);
            preparedStatement.setLong(1, id);

            Person person = get(id);
            preparedStatement.executeUpdate();

            return person;
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

        String sqlUpdate = "UPDATE person SET " +
                "pid = ?, username = ?, pass = ?, email = ?, " +
                "first_name = ?, last_name = ?, occupation = ?, birth_date = ? " +
                "WHERE pid = ?";

        try( Connection connection = DriverManager.getConnection( url, username, password )){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setString(5, entity.getFirstName());
            preparedStatement.setString(6, entity.getLastName());
            preparedStatement.setString(7, entity.getOccupation());
            preparedStatement.setDate(8, java.sql.Date.valueOf(entity.getDateOfBirth()));

            preparedStatement.setLong( 9, entity.getId() );
            preparedStatement.executeUpdate();

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Person get(Long id) throws RepoException {

        if ( id == null ){
            throw new RepoException("<<id is null>>\n");
        }

        String sqlGet = "SELECT * FROM person WHERE pid = ?";
        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if ( !resultSet.next() ){
                throw new RepoException("<<Entity with id does not exist>>\n");
            }

            Person person = new Person(
                    resultSet.getLong("pid"),
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

            return person;

        }
        catch ( SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public List<Person> getAll() {

        String sqlGet = "SELECT * FROM person";
        try( Connection connection = DriverManager.getConnection(url, username, password) ){

            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Person> persons = new ArrayList<>();
            while( resultSet.next() ){

                Person person = new Person(
                        resultSet.getLong("pid"),
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
}
