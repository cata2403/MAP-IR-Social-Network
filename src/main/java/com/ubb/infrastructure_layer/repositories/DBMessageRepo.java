package com.ubb.infrastructure_layer.repositories;

import com.ubb.domain_layer.entities.Message;
import com.ubb.domain_layer.entities.User;
import com.ubb.exceptions.RepoException;
import com.ubb.infrastructure_layer.utils.DateTimeFormats;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBMessageRepo implements MessageRepo {

    private final String url;
    private final String username;
    private final String password;

    public DBMessageRepo(String url, String user, String password) {
        this.url = url;
        this.username = user;
        this.password = password;
    }

    @Override
    public void add(Message entity) throws RepoException {

        if(entity == null)
            throw new RepoException("The message is null");

        String addSql = "INSERT INTO user_message(mid, uid, send_date, mess, reply) " +
                "VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection( url, username, password )){
            connection.setAutoCommit(false);
            try{

                PreparedStatement preparedStatement = connection.prepareStatement(addSql);
                preparedStatement.setLong(1, entity.getId());
                preparedStatement.setLong(2, entity.getSender().getId());
                preparedStatement.setTimestamp(
                        3, java.sql.Timestamp.valueOf(entity.getDate())
                );
                preparedStatement.setString(4, entity.getMessage());
                if (entity.getReply() != null) {
                    preparedStatement.setLong(5, entity.getReply().getId());
                }
                else preparedStatement.setNull(5, java.sql.Types.BIGINT);

                preparedStatement.executeUpdate();

                for(User user : entity.getReceiver())
                {
                    String sql = "INSERT INTO receive_message(uid, mid) VALUES (?,?)";
                    PreparedStatement pS =  connection.prepareStatement(sql);
                    pS.setLong(1, user.getId());
                    pS.setLong(2, entity.getId());
                    pS.executeUpdate();
                }

                connection.commit();
            }
            catch(SQLException e){
                connection.rollback();
                throw new RepoException(e.getMessage());
            }
        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    public List<Message> findAllInChat(Long userId1, Long userId2) throws RepoException {

        if (userId1 == null || userId2 == null)
            throw new RepoException("Some ids are null");
        String sql = "select um.mid, um.mess, um.send_date, um.reply " +
                "from user_message as um join  receive_message as rm on um.mid = rm.mid " +
                "where (um.uid = ? and rm.uid = ?) or (um.uid = ? and rm.uid = ?)";
        try(Connection connection = DriverManager.getConnection(url,username,password)){

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId1);
            preparedStatement.setLong(2, userId2);
            preparedStatement.setLong(3, userId2);
            preparedStatement.setLong(4, userId1);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Message> messages = new ArrayList<>();
            while (resultSet.next()) {
                Message message = new Message(
                        resultSet.getLong("mid"),
                        resultSet.getString("mess")
                );
                message.setDate(resultSet.getTimestamp("send_date").toLocalDateTime());
                Long repId = resultSet.getLong("reply");
                if(!resultSet.wasNull()){
                    message.setReply(
                            get(repId).get()
                    );
                }
                messages.add(message);
            }
            return messages;
        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Optional<Message> delete(Long id) throws RepoException {

        String sql1 = "DELETE FROM receive_message WHERE mid = ?";
        String sql3 = "UPDATE user_message SET reply = NULL WHERE reply = ?";
        String sql2 = "DELETE FROM user_message WHERE mid = ?";

        try(Connection connection = DriverManager.getConnection(url,username,password)){

            connection.setAutoCommit(false);
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql1);
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);){

                preparedStatement3.setLong(1, id);
                preparedStatement3.executeUpdate();

                Message message = get(id).get();
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                preparedStatement2.setLong(1, id);
                preparedStatement2.executeUpdate();
                connection.commit();
                return Optional.of(message);
            }
            catch (SQLException error){
                connection.rollback();
                throw new RepoException(error.getMessage());
            }

        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public void update(Message entity) throws RepoException {
        if(entity == null)
            throw new RepoException("The message is null");
        String sql = "UPDATE user_message SET send_date = ?, mess = ? WHERE mid = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)){

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(
                    1,java.sql.Timestamp.valueOf(entity.getDate())
            );
            preparedStatement.setString(2, entity.getMessage());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    public List<Long> getReceiversIds(Long id){
        String getReceiversSql = "SELECT uid FROM receive_message WHERE mid = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password)) {

            PreparedStatement preparedStatement = connection.prepareStatement(getReceiversSql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Long> ids = new ArrayList<>();
            while (resultSet.next()) {
                ids.add(resultSet.getLong("uid"));
            }

            return ids;
        }
        catch(SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    public Long getSenderId(Long id){
        String getSql = "SELECT uid FROM user_message WHERE mid = ?";
        try(Connection connection = DriverManager.getConnection( url, username, password )){

            PreparedStatement preparedStatement = connection.prepareStatement(getSql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new RepoException("The message does not exist");
            }
            return resultSet.getLong("uid");
        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Optional<Message> get(Long id) throws RepoException {

        if (id == null)
            throw new RepoException("The id is null");
        String getSql = "SELECT * FROM user_message WHERE mid = ?";
        try(Connection connection = DriverManager.getConnection( url, username, password )){

            PreparedStatement preparedStatement = connection.prepareStatement(getSql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new RepoException("The message does not exist");
            }
            Message message = new Message(
                    resultSet.getLong("mid"),
                    resultSet.getString("mess")
            );
            message.setDate(resultSet.getTimestamp("send_date").toLocalDateTime());

            Long repId = resultSet.getLong("reply");
            if(!resultSet.wasNull()){
                message.setReply(
                        get(repId).get()
                );
            }
            return Optional.of(message);
        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }

    @Override
    public Iterable<Message> getAll() {

        String getSql = "SELECT * FROM user_message";
        try(Connection connection = DriverManager.getConnection( url, username, password )){

            PreparedStatement preparedStatement = connection.prepareStatement(getSql);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Message> messages = new ArrayList<>();
            while (resultSet.next()) {
                Message message = new Message(
                        resultSet.getLong("mid"),
                        resultSet.getString("mess")
                );
                message.setDate(resultSet.getTimestamp("send_date").toLocalDateTime());
                Long repId = resultSet.getLong("reply");
                if(!resultSet.wasNull()){
                    message.setReply(
                            get(repId).get()
                    );
                }
                messages.add(message);
            }
            return messages;
        }
        catch (SQLException error){
            throw new RepoException(error.getMessage());
        }
    }
}

