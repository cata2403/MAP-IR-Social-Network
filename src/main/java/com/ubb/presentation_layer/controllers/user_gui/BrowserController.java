package com.ubb.presentation_layer.controllers.user_gui;

import com.ubb.application_layer.services.GeneralUserService;
import com.ubb.domain_layer.enums.UserType;
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

public class BrowserController implements Observer {

    private ObservableList<UserDTO> usersModel = FXCollections.observableArrayList();
    @FXML
    private TableView<UserDTO> tableView;
    @FXML
    private TableColumn<UserDTO,String> usernameCol;
    @FXML
    private TableColumn<UserDTO, UserType> typeCol;
    private GeneralUserService service;
    private LoginDTO userData;

    @FXML
    public void initialize(){
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));
        tableView.setItems(usersModel);
    }

    private void init(){
        List<UserDTO> data = service.listUsers();
        usersModel.clear();
        usersModel.addAll(data);
    }

    public void setService(GeneralUserService service,  LoginDTO userData){
        this.service = service;
        this.userData = userData;
        init();
    }

    private void showErrorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    public void handleAddFriendRequestButton(){
        if (tableView.getSelectionModel().getSelectedItem() != null){
            Long myId = userData.user().getId();
            Long otherId = tableView.getSelectionModel().getSelectedItem().getId();
            try{
                service.sendFriendRequest(myId,otherId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Friend request has been sent");
            }
            catch (Exception e){
                showErrorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void update(ChangeEvent changeEvent) {
        if (ChangeEvent.USERS_UPDATE.equals(changeEvent)) {
            init();
        }
    }
}
