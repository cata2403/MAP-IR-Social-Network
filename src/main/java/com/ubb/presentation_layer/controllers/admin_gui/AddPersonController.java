package com.ubb.presentation_layer.controllers.admin_gui;

import com.ubb.application_layer.services.AdminService;
import com.ubb.dtos.FullUserInfoDTO;
import com.ubb.dtos.PersonExtrasDTO;
import com.ubb.infrastructure_layer.utils.DateTimeFormats;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddPersonController {

    AdminService service;

    public void setService(AdminService service){
        this.service = service;
    }

    @FXML
    public DatePicker datePicker;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passField;
    @FXML
    public TextField fnameField;
    @FXML
    public TextField lnameField;
    @FXML
    public TextField occupationField;

    private void alertEmptyField(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warning");
        alert.setContentText("You left an empty field");
        alert.showAndWait();
    }

    public void handleAddButton(){

        try{
            String username = usernameField.getText();
            if(username.isEmpty()){
                alertEmptyField();
                return;
            }

            String email = emailField.getText();
            if(email.isEmpty()){
                alertEmptyField();
                return;
            }

            String pass = passField.getText();
            if(pass.isEmpty()){
                alertEmptyField();
                return;
            }

            String firstName = fnameField.getText();
            if(firstName.isEmpty()){
                alertEmptyField();
                return;
            }

            String lastName = lnameField.getText();
            if(lastName.isEmpty()){
                alertEmptyField();
                return;
            }

            String occupation = occupationField.getText();
            if(occupation.isEmpty()){
                alertEmptyField();
                return;
            }

            LocalDate dob = datePicker.getValue();
            if(dob == null){
                alertEmptyField();
                return;
            }

            FullUserInfoDTO dto1 = new FullUserInfoDTO(
                    username, email, pass
            );
            PersonExtrasDTO dto2 = new PersonExtrasDTO(
                    firstName, lastName, occupation, dob.format(DateTimeFormats.getDateFormatter1())
            );

            service.createUserPerson(dto1, dto2);
        }
        catch(Exception error){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(error.getMessage());
            alert.showAndWait();
            return;
        }


        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();
    }
}

