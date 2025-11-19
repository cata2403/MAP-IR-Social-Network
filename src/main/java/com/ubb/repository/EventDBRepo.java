package com.ubb.repository;

import com.ubb.domain.entities.Event;
import com.ubb.domain.entities.RaceEvent;
import com.ubb.utils.events_strategies.DynaminProgrammingStrategy;
import com.ubb.utils.events_strategies.SolvingStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDBRepo implements Repository<Long, Event>{

    String url;
    String username;
    String password;

    public EventDBRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Double[] getLanes( Event entity ) {

        List<Double> data = ((RaceEvent) entity).getLanes();
        Double[] lanes = new Double[data.size()];
        for(int i = 0; i < data.size(); i++){
            lanes[i] = data.get(i);
        }

        return lanes;
    }

    @Override
    public void add(Event entity) throws RepoException {

        if ( entity == null )
            throw new RepoException("Event is null");

        String sqlInsert = "INSERT INTO app_event(eid, eName, min_flock_size, strategy, lanes) " +
                "VALUES (?, ?, ?, ?, ?)";
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

        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Event delete(Long id) throws RepoException {

        if ( id == null )
            throw new RepoException("Event id is null");

        String sqlDelete = "DELETE FROM app_event WHERE eid = ?";

        try( Connection connection = DriverManager.getConnection( url, username, password ) ) {

            PreparedStatement statement = connection.prepareStatement(sqlDelete);
            statement.setLong(1, id);

            Event event = get(id);
            statement.executeUpdate();

            return event;

        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public void update(Event entity) throws RepoException {

        if ( entity == null )
            throw new RepoException("Event is null");

        String sqlUpdate =  "UPDATE app_event SET" +
                " eName = ?, min_flock_size = ?, strategy = ?, lanes = ?" +
                " WHERE eid = ?";
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
        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Event get(Long id) throws RepoException {

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
            if (rs.getString("strategy") == "dp")
                strategy = new DynaminProgrammingStrategy();
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

            return event;

        }
        catch ( SQLException error ){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public List<Event> getAll() {

        String sqlGet = "SELECT * FROM app_event";
        try( Connection connection = DriverManager.getConnection( url, username, password ) ){

            PreparedStatement statement = connection.prepareStatement(sqlGet);
            ResultSet rs = statement.executeQuery();
            List<Event> events = new ArrayList<>();
            while( rs.next() ){

                SolvingStrategy strategy;
                if ("dp".equals(rs.getString("strategy")))
                    strategy = new DynaminProgrammingStrategy();
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
}
