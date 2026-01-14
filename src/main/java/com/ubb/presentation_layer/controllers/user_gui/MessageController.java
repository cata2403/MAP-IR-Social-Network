package com.ubb.presentation_layer.controllers.user_gui;

import com.ubb.application_layer.services.GeneralUserService;
import com.ubb.domain_layer.entities.Message;
import com.ubb.domain_layer.entities.User;
import com.ubb.dtos.LoginDTO;
import com.ubb.observer.ChangeEvent;
import com.ubb.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MessageController implements Observer {

    private final ObservableList<Message> messagesModel = FXCollections.observableArrayList();
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();
    @FXML
    private ListView<User> friendList;
    @FXML
    private ListView<Message> messageList;
    @FXML
    private TextArea messTextArea;
    GeneralUserService service;
    LoginDTO data;

    public void setService(GeneralUserService service, LoginDTO data) {
        this.service = service;
        this.data = data;
        service.addObserver(this);
        initFriends();
    }

    @FXML
    public void initialize() {
        friendList.setItems(usersModel);
        messageList.setItems(messagesModel);
        friendList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            initMessages();
        });
    }

    private void showErrorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    private void initFriends(){
        List<User> friends = service.getFriendsOfUser(data.user().getId());
        usersModel.clear();
        usersModel.addAll(friends);
    }

    private void initMessages(){
        if (friendList.getSelectionModel().getSelectedItem()!=null){
            User from = data.user();
            User to = friendList.getSelectionModel().getSelectedItem();
            List<Message> messages = service.obtainChatMessages(from.getId(), to.getId());
            messagesModel.clear();
            messagesModel.addAll(messages);
        }
        else messagesModel.clear();
    }

    public void handleSendButton(){
        String text =  messTextArea.getText();
        User from = data.user();
        User to = friendList.getSelectionModel().getSelectedItem();
        try{
            service.sendMessage(text,from.getId(),List.of(to.getId()),null);
        }
        catch (Exception e){
            showErrorMessage(e.getMessage());
        }
    }

    public void handleSendAllButton(){
        String text =  messTextArea.getText();
        User from = data.user();
        try{
            List<Long> to = new ArrayList<>();
            service.getFriendsOfUser(from.getId()).forEach(user -> to.add(user.getId()));
            service.sendMessage(text,from.getId(),to,null);
        }
        catch (Exception e){
            showErrorMessage(e.getMessage());
        }
    }

    public void handleDeleteButton(){
        if(messageList.getSelectionModel().getSelectedItem()!=null){
            Message message = messageList.getSelectionModel().getSelectedItem();
            try{
                service.deleteMessage(message.getId(), data.user().getId());
            }
            catch (Exception e){
                e.printStackTrace();
                showErrorMessage(e.getMessage());
            }
        }
    }

    public void handleDeleteFriendButton(){
        if(friendList.getSelectionModel().getSelectedItem()!=null){
            Long myId = data.user().getId();
            Long friendId = friendList.getSelectionModel().getSelectedItem().getId();
            try{
                service.deleteFriend(myId,friendId);
            }
            catch (Exception e){
                showErrorMessage(e.getMessage());
            }
        }
    }

    public void handleReplyButton(){
        if(messageList.getSelectionModel().getSelectedItem()!=null){
            Message message = messageList.getSelectionModel().getSelectedItem();
            String text =  messTextArea.getText();
            User from = data.user();
            User to = friendList.getSelectionModel().getSelectedItem();
            try{
                service.sendMessage(text,from.getId(),List.of(to.getId()),message);
            }
            catch (Exception e){
                showErrorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void update(ChangeEvent event) {
        if (ChangeEvent.MESSAGES_UPDATE.equals(event))
            initMessages();
        if (ChangeEvent.FRIENDSHIPS_UPDATE.equals(event))
            initFriends();
        if (ChangeEvent.USERS_UPDATE.equals(event))
            initMessages();
    }
}
