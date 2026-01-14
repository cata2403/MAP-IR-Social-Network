package com.ubb.presentation_layer.controllers.login_gui;

import com.ubb.application_layer.services.PersonService;
import com.ubb.dtos.FullUserInfoDTO;
import com.ubb.dtos.LoginDTO;
import com.ubb.dtos.PersonExtrasDTO;
import com.ubb.infrastructure_layer.utils.DateTimeFormats;
import com.ubb.presentation_layer.controllers.user_gui.PersonController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PersonSignupController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passField;
    @FXML
    private TextField fnameField;
    @FXML
    private TextField lnameField;
    @FXML
    private TextField occupationField;
    private PersonService personService;

    public void setService(PersonService service) {
        personService = service;
    }

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

            LoginDTO dto = personService.signUp(dto1, dto2);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user_fxmls/person_home_page.fxml"));
            Parent root = loader.load();
            PersonController controller = loader.getController();
            controller.setService(personService, dto);
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root));
            stage2.setResizable(false);
            stage2.show();
        }
        catch(Exception error){
            error.printStackTrace();
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
