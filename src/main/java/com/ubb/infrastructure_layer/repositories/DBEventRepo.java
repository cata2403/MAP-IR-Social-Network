package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.Duck;
import com.ubb.domain_layer.entities.RaceEvent;
import com.ubb.domain_layer.entities.User;
import com.ubb.exceptions.RepoException;
import com.ubb.infrastructure_layer.utils.event_strategies.DynamicProgrammingStrategy;
import com.ubb.infrastructure_layer.utils.event_strategies.SolvingStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBEventRepo implements  EventRepo{

    private String url;
    private String username;
    private String password;

    public DBEventRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Double[] getLanes( RaceEvent entity ) {

        List<Double> data = entity.getLanes();
        Double[] lanes = new Double[data.size()];
        for(int i = 0; i < data.size(); i++){
            lanes[i] = data.get(i);
        }

        return lanes;
    }

    @Override
    public void add(RaceEvent entity) throws RepoException {

        if ( entity == null )
            throw new RepoException("Event is null");

        String sqlInsert = "INSERT INTO app_event(eid, eName, min_flock_size, strategy, lanes) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sqlInsert2 = "INSERT INTO event_subscription(uid, eid, s_type) VALUES (?,?,'spectator')";
        String sqlInsert3 = "INSERT INTO event_subscription(uid, eid, s_type) VALUES (?,?,'participant')";
        try(Connection connection = DriverManager.getConnection( url, username, password )){

            PreparedStatement statement = connection.prepareStatement(sqlInsert);
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getEventName());
            statement.setInt(3, entity.getMinFlockSize());
            statement.setString(4, "dp");

            Array array = connection.createArrayOf(
                    "FLOAT8", getLanes(entity)
            );
            statement.setArray(5, array);

            statement.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement(sqlInsert2);
            statement2.setLong(2, entity.getId());
            for(User user : entity.getObservers()){
                statement2.setLong(1, user.getId());
                statement2.executeUpdate();
            }

            PreparedStatement statement3 = connection.prepareStatement(sqlInsert3);
            statement3.setLong(2, entity.getId());
            for(Duck duck : entity.getParticipants()){
                statement3.setLong(1, duck.getId());
                statement3.executeUpdate();
            }
        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Optional<RaceEvent> delete(Long id) throws RepoException {

        String sqlDelete = "DELETE FROM event_subscription WHERE eid = ?";
        String sqlDelete2 = "DELETE FROM app_event WHERE eid = ?";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ) {

            PreparedStatement statement = connection.prepareStatement(sqlDelete);
            statement.setLong(1, id);

            RaceEvent event = get(id).get();
            statement.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement(sqlDelete2);
            statement2.setLong(1, id);
            statement2.executeUpdate();

            return Optional.of(event);

        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public void update(RaceEvent entity) throws RepoException {

        if ( entity == null )
            throw new RepoException("Event is null");

        String sqlUpdate =  "UPDATE app_event SET" +
                " eName = ?, min_flock_size = ?, strategy = ?, lanes = ?" +
                " WHERE eid = ?";
        String sqlInsert2 = "INSERT INTO event_subscription(uid, eid, s_type) VALUES (?,?,'spectator')";
        String sqlInsert3 = "INSERT INTO event_subscription(uid, eid, s_type) VALUES (?,?,'participant')";
        String sqlExista = "SELECT * FROM event_subscription WHERE eid = ? and uid = ? and s_type = ?";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ) {

            PreparedStatement statement = connection.prepareStatement(sqlUpdate);
            statement.setString(1, entity.getEventName());
            statement.setInt(2, entity.getMinFlockSize());
            statement.setString(3, "dp");

            Array array = connection.createArrayOf(
                    "FLOAT8", getLanes(entity)
            );
            statement.setArray(4, array);

            statement.setLong(5, entity.getId());
            statement.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement(sqlInsert2);
            PreparedStatement statement3 = connection.prepareStatement(sqlInsert3);
            PreparedStatement statement4 = connection.prepareStatement(sqlExista);
            statement2.setLong(2, entity.getId());
            statement3.setLong(2, entity.getId());
            statement4.setLong(1, entity.getId());

            statement4.setString(3, "spectator");
            for(User user : entity.getObservers()){
                statement4.setLong(2, user.getId());
                ResultSet resultSet = statement4.executeQuery();
                if(!resultSet.next()){
                    statement2.setLong(1, user.getId());
                    statement2.executeUpdate();
                }
            }

            statement4.setString(3, "participant");
            for(Duck duck : entity.getParticipants()){
                statement4.setLong(2, duck.getId());
                ResultSet resultSet = statement4.executeQuery();
                if(!resultSet.next()){
                    statement3.setLong(1, duck.getId());
                    statement3.executeUpdate();
                }
            }
        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Optional<RaceEvent> get(Long id) throws RepoException {

        if  ( id == null )
            throw new RepoException("Event id is null");

        String sqlGet = "SELECT * FROM app_event WHERE eid = ?";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement statement = connection.prepareStatement(sqlGet);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if( !rs.next() ){
                throw new RepoException("Event not found");
            }

            SolvingStrategy strategy;
            if ("dp".equals(rs.getString("strategy")))
                strategy = new DynamicProgrammingStrategy();
            else
                throw new RepoException("<<Strategia din DB salvata prost>>");

            RaceEvent event = new RaceEvent(
                    rs.getLong("eid"),
                    rs.getString("eName"),
                    strategy
            );

            Array sqlArray = rs.getArray("lanes");
            Double[] data = (Double[]) sqlArray.getArray();

            for(int i = 0; i < data.length; i++){
                event.addLane(data[i]);
            }

            return Optional.of(event);

        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Iterable<RaceEvent> getAll() {


        String sqlGet = "SELECT * FROM app_event";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement statement = connection.prepareStatement(sqlGet);
            ResultSet rs = statement.executeQuery();
            List<RaceEvent> events = new ArrayList<>();
            while( rs.next() ){

                SolvingStrategy strategy;
                if ("dp".equals(rs.getString("strategy")))
                    strategy = new DynamicProgrammingStrategy();
                else
                    throw new RepoException("<<Strategia din DB salvata prost>>");

                RaceEvent event = new RaceEvent(
                        rs.getLong("eid"),
                        rs.getString("eName"),
                        strategy
                );

                Array sqlArray = rs.getArray("lanes");
                Double[] data = (Double[]) sqlArray.getArray();

                for(int i = 0; i < data.length; i++){
                    event.addLane(data[i]);
                }

                events.add( event );
            }

            return events;

        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public List<Long> getSpectators(Long id) {
        String sql = "select * from event_subscription where eid = ? and s_type = 'spectator'";
        try(Connection connection = DriverManager.getConnection( url, username, password ) ){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Long> uids = new ArrayList<>();
            while( rs.next() ){
                uids.add(rs.getLong("uid"));
            }
            return uids;
        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    public List<Long> getParticipants(Long id){

        String sql = "select * from event_subscription where eid = ? and s_type = 'participant'";
        try(Connection connection = DriverManager.getConnection( url, username, password ) ){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Long> uids = new ArrayList<>();
            while( rs.next() ){
                uids.add(rs.getLong("uid"));
            }
            return uids;
        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }
}
