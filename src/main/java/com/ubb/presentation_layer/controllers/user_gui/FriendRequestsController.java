package com.ubb.presentation_layer.controllers.user_gui;

import com.ubb.application_layer.services.GeneralUserService;
import com.ubb.domain_layer.enums.FriendRequest;
import com.ubb.dtos.LoginDTO;
import com.ubb.dtos.UserDTO;
import com.ubb.observer.ChangeEvent;
import com.ubb.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class FriendRequestsController implements Observer {

    private ObservableList<UserDTO> friendRequests = FXCollections.observableArrayList();
    @FXML
    private TableView<UserDTO> tableView;
    @FXML
    private TableColumn<UserDTO,String> usernameCol;
    private GeneralUserService service;
    private LoginDTO userData;

    public void setService(GeneralUserService service,  LoginDTO userData) {
        this.service = service;
        this.userData = userData;
        service.addObserver(this);
        init();
    }

    @FXML
    private void initialize(){
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableView.setItems(friendRequests);
    }

    private void init(){
        List<UserDTO> data = service.getFriendRequests(userData.user().getId());
        friendRequests.clear();
        friendRequests.addAll(data);
    }

    private void showErrorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    public void handleAcceptButton(){
        if (tableView.getSelectionModel().getSelectedItem() != null){
            Long myId = userData.user().getId();
            String friendUsername = tableView.getSelectionModel().getSelectedItem().getUsername();
            try{
                service.acceptFriendRequest(myId,friendUsername, FriendRequest.ACCEPTED);
            }
            catch (Exception e){
                showErrorMessage(e.getMessage());
            }
        }
    }

    public void handleDeclineButton(){
        if (tableView.getSelectionModel().getSelectedItem() != null){
            Long myId = userData.user().getId();
            Long friendId = tableView.getSelectionModel().getSelectedItem().getId();
            try{
                service.deleteFriend(myId,friendId);
            }
            catch (Exception e){
                showErrorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        if (ChangeEvent.FRIENDSHIPS_UPDATE.equals(changeEvent)) {
            init();
        }
        if (ChangeEvent.USERS_UPDATE.equals(changeEvent)) {
            init();
        }
    }
}

