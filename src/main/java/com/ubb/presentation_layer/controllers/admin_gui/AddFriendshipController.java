package com.ubb.presentation_layer.controllers.admin_gui;

import com.ubb.application_layer.services.AdminService;
import com.ubb.domain_layer.entities.User;
import com.ubb.domain_layer.enums.FriendRequest;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddFriendshipController {

    public TextField u1Field;
    public TextField u2Field;
    public ComboBox<String> cbRtype;
    AdminService service;

    public void setService(AdminService service){
        this.service = service;
    }

    private void alertEmptyField(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warning");
        alert.setContentText("You left an empty field");
        alert.showAndWait();
    }

    public void handlerAddButton(){
        try{
            String username1  = u1Field.getText();
            String username2  = u2Field.getText();
            String requestType =  cbRtype.getSelectionModel().getSelectedItem();

            if ( username1.isEmpty() || username2.isEmpty() || requestType == null ){
                alertEmptyField();
                return;
            }

            User user1 = service.obtainUserByUsername(username1);
            User user2 = service.obtainUserByUsername(username2);
            service.createFriendship(
                    user1.getId(), user2.getId(), FriendRequest.valueOf(requestType)
            );

        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        Stage stage = (Stage) cbRtype.getScene().getWindow();
        stage.close();
    }
}

